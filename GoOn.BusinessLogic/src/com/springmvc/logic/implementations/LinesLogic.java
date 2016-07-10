package com.springmvc.logic.implementations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Asiento;
import com.springmvc.entities.tenant.Devolucion;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.entities.tenant.ViajesBuscados;
import com.springmvc.enums.DayOfWeek;
import com.springmvc.enums.PackageStatus;
import com.springmvc.enums.TicketStatus;
import com.springmvc.exceptions.BusInServiceException;
import com.springmvc.exceptions.BusTravelConcurrencyException;
import com.springmvc.exceptions.BusyDriverException;
import com.springmvc.exceptions.CollectTicketException;
import com.springmvc.logic.interfaces.ILinesLogic;

public class LinesLogic implements ILinesLogic
{
	
	private TenantDAContext TenantContext;
	
	public LinesLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant, false);
	}

	public void createTerminal(Parada terminal) 
	{
		terminal.setEs_terminal(true);
		TenantContext.ParadaRepository.InsertStation(terminal);		
	}

	public List<Parada> GetTerminals() 
	{
		return TenantContext.ParadaRepository.GetTerminals();	
	}
	
	public void insertLine(Linea linea)
	{
		TenantContext.LineaRepository.InsertLine(linea);
	}
	
	public Linea findByID(long id_linea)
	{
		return TenantContext.LineaRepository.findByID(id_linea);
	}

	public Parada findParadaByID(long id_parada) 
	{
		return TenantContext.ParadaRepository.findByID(id_parada);
	}

	public List<Linea> getLineas() 
	{
		return TenantContext.LineaRepository.getLineas();
	}

	public boolean TieneViajes(long id_linea) 
	{
		return TenantContext.LineaRepository.TieneViajes(id_linea);
	}

	public void deleteLinea(long id_linea) 
	{
		TenantContext.LineaRepository.deleteLinea(id_linea);		
	}

	public int CreateTravels(Viaje travel, Map<DayOfWeek, Boolean> days, Calendar dayFrom, Calendar dayTo, Date time) 
			throws BusInServiceException, BusTravelConcurrencyException, BusyDriverException 
	{
		int createdTravels = 0;
		boolean dayInPeriod = true;
		List<Viaje> travelsToPersist = new ArrayList<>();
		while (dayInPeriod) 
		{
			int dayOfWeek = dayFrom.get((GregorianCalendar.DAY_OF_WEEK));
			if(days.get(DayOfWeek.values()[dayOfWeek - 1]))
			{
				dayFrom.getTimeZone();
				Date travelDate = dayFrom.getTime();
				travelDate.setHours(time.getHours());
				travelDate.setMinutes(time.getMinutes());
				
				CheckBusAvailability(travel, travelDate);
				CheckDriverAvailability(travel, travelDate);
				
				Viaje travelToPersist = new Viaje();
				travelToPersist.setTerminado(false);
				travelToPersist.setConductor(travel.getConductor());
				travelToPersist.setLinea(travel.getLinea());
				travelToPersist.setVehiculo(travel.getVehiculo());
				travelToPersist.setInicio(travelDate);	
				travelsToPersist.add(travelToPersist);
			}
			dayFrom.add((Calendar.DATE), 1);
			dayFrom.set(Calendar.MILLISECOND, 0);
			dayFrom.set(Calendar.HOUR , 0);
			dayFrom.set(Calendar.MINUTE , 0);
			dayFrom.set(Calendar.SECOND , 0);
			dayInPeriod = !dayTo.before(dayFrom);
		}
		for (Viaje travelt : travelsToPersist) 
		{
			TenantContext.ViajeRepository.InsertTravel(travelt);
			createdTravels++;
		}
		return createdTravels;
	}

	private void CheckBusAvailability(Viaje travel, Date travelDate) throws BusInServiceException, BusTravelConcurrencyException
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		MantenimientoLogic mm = new MantenimientoLogic(TenantContext.TenantName);
		Calendar beginTravel = Calendar.getInstance();
		beginTravel.setTime(travelDate);
		beginTravel.set(GregorianCalendar.HOUR_OF_DAY, travelDate.getHours());
		beginTravel.set(GregorianCalendar.MINUTE, travelDate.getMinutes());
		Calendar endTravel = Calendar.getInstance();
		endTravel.setTime(travelDate);
		endTravel.set(GregorianCalendar.HOUR_OF_DAY, travelDate.getHours()); 
		endTravel.set(GregorianCalendar.MINUTE, travelDate.getMinutes());
		endTravel.add(GregorianCalendar.MINUTE, travel.getLinea().getTiempo_estimado() + 10);
		List<Mantenimiento> services = mm.findServiceByDate(travel.getVehiculo().getId_vehiculo(), beginTravel, endTravel);
		if(!services.isEmpty())
		{
			throw new BusInServiceException("El omnibus estará en mantenimiento el " + df.format(travelDate));
		}
		List<Viaje> travels = GetBusTravels(travel.getVehiculo().getId_vehiculo(), beginTravel, endTravel);
		if(!travels.isEmpty())
		{
			throw new BusTravelConcurrencyException("El ómnibus ya posee viajes entre " + df.format(travelDate)  + " y " + df.format(endTravel.getTime()));
		}
	}
	
	private void CheckDriverAvailability(Viaje travel, Date travelDate) throws BusyDriverException
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Calendar beginTravel = Calendar.getInstance();
		beginTravel.setTime(travelDate);
		beginTravel.set(GregorianCalendar.HOUR_OF_DAY, travelDate.getHours());
		beginTravel.set(GregorianCalendar.MINUTE, travelDate.getMinutes());
		Calendar endTravel = Calendar.getInstance();
		endTravel.setTime(travelDate);
		endTravel.set(GregorianCalendar.HOUR_OF_DAY, travelDate.getHours()); 
		endTravel.set(GregorianCalendar.MINUTE, travelDate.getMinutes());
		endTravel.add(GregorianCalendar.MINUTE, travel.getLinea().getTiempo_estimado() + 10);
		
		List<Viaje> travels = GetDriverTravels(travel.getConductor().getIdUsuario(), beginTravel, endTravel);
		if(!travels.isEmpty())
		{
			throw new BusyDriverException("El conductor ya posee viajes a entre " + df.format(travelDate) + " y " + df.format(endTravel.getTime()));
		}
	}
	
	private List<Viaje> GetDriverTravels(long userId, Calendar beginTravel, Calendar endTravel) 
	{
		return TenantContext.ViajeRepository.GetByDiverAndDate(userId, beginTravel.getTime(), endTravel.getTime());
	}

	public List<Viaje> GetBusTravels(long id_vehiculo, Calendar beginTravel, Calendar endTravel) 
	{
		return TenantContext.ViajeRepository.GetByBus(id_vehiculo, beginTravel.getTime(), endTravel.getTime());
	}

	public List<Viaje> GetTravels(Date filterDate) 
	{
		return TenantContext.ViajeRepository.GetTravels(filterDate);
	}

	public List<Viaje> GetPackageTravels(Parada origin, Parada destination) 
	{
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 1);
		tomorrow.set(GregorianCalendar.HOUR, 0);
		Calendar limit = Calendar.getInstance();
		limit.add(GregorianCalendar.DAY_OF_YEAR, 15);
		return TenantContext.ViajeRepository.GetPackageTravels(origin.getId_parada(), destination.getId_parada(), tomorrow, limit);
	}
	
	public void deleteTravel(long travelId) 
	{
		TenantContext.ViajeRepository.DeleteTravel(travelId);
	}

	public boolean IsTravelInUse(long travelId) 
	{
		return TenantContext.ViajeRepository.HasPackages(travelId)
			|| TenantContext.ViajeRepository.HasTickets(travelId);
	}

	public List<Parada> GetParadas()
	{
		return TenantContext.ParadaRepository.FindAll();
	}
	
	public List<Viaje> SearchTravels(Calendar dateFrom, Calendar dateTo, List<Integer> origins, List<Integer> destinations) 
	{
		//me quedo con las lineas correspondiente a la lista de origenes seleccionados
		List<Long> id_lineas = TenantContext.LineaRepository.getidLineasbyIdOrigins(origins);
		List<Viaje> viajes = TenantContext.ViajeRepository.GetLineTravels(id_lineas, dateFrom);
		return viajes;
	}
	
	public List<ViajesBuscados> SearchTravelsAdvanced(Calendar dateFrom, Calendar dateTo, List<Integer> origins, List<Integer> destinations)
	{		
		List<ViajesBuscados> viajes = TenantContext.ViajeRepository.getTravelsAdvanced(origins, destinations, dateFrom);
		for(int x = 0; x < viajes.size(); x ++)
		{
			int cant_vendidos = TenantContext.PasajeRepository.getCantVendidos(viajes.get(x).getOrigen(), viajes.get(x).getDestino(), 
																			   viajes.get(x).getLinea_id_linea(), viajes.get(x).getId_viaje());
			int cant_total = viajes.get(x).getCantasientos();
			viajes.get(x).setCantasientos(cant_total - cant_vendidos);
			Double valor = TenantContext.PasajeRepository.getValorPasaje(viajes.get(x).getOrigen(), viajes.get(x).getDestino(),viajes.get(x).getLinea_id_linea());
			viajes.get(x).setValor(valor);
		}
		return viajes;		
	}

	public List<Parada> GetStationsByDestinations(List<Integer> destinations) 
	{		
		
		List<Parada> stations = new ArrayList<Parada>();
		List<Parada> auxstations = new ArrayList<Parada>();
		
		for(int x = 0; x < destinations.size(); x++)
		{
			auxstations = TenantContext.ParadaRepository.findStationsByDestinations(destinations.get(x));
			for(int y = 0; y < auxstations.size(); y++)
			{				  
				agregoParadaSiNoEsta(stations, auxstations.get(y));
			}
		}	
		//sacar paradas del circulo - por si entran mas de una parada de la misma linea - dificil pero puede ser
		for(int x = 0; x < destinations.size(); x++)
		{
			sacoParadaSiEsta(stations, destinations.get(x));
		}
		return stations;
	}
	
	/*Estas dos funciones deberian estar en un "utiles"*/
	public boolean paradaPerteneceALinea(long idParada, Linea linea)
	{
		for(int i = 0; i < linea.getParadas().size(); i++) //recorre las lineas y si la parada pertenece, tira todas las anteriores
		{
			if(linea.getParadas().get(i).getId_parada() == idParada)
			{
				return true;
			}
		}
		return false;
	}
	
	public void agregoParadaSiNoEsta(List<Parada> paradas, Parada parada)
	{
		boolean esta = false;
		for(int i = 0; i < paradas.size(); i++)
		{
			if(parada.getId_parada() == paradas.get(i).getId_parada())
			{
				esta = true;
			}	
		}
		if(esta == false)
		{
			paradas.add(parada);
		}
	}
	
	public void sacoParadaSiEsta(List<Parada> paradas, int id_parada)
	{
		//recorre con iterator porque es seguro hacer delete durante la iteracion
		for (Iterator<Parada> iterator = paradas.iterator(); iterator.hasNext();) 
		{
		    Parada parada = iterator.next();
		    if (parada.getId_parada() == id_parada) 
		    {
		        iterator.remove();
		    }
		}
	}

	public List<Parada> GetOriginsFromDestination(Parada parada) 
	{
		return TenantContext.ParadaRepository.findOriginTerminalsByDestination(parada.getId_parada());
	}

	public List<Parada> GetPackageTerminals() 
	{
		return TenantContext.ParadaRepository.GetPackageTerminals();
	}

	public List<Parada> GetDestinationsFromOrigin(Parada parada) 
	{
		return TenantContext.ParadaRepository.findDestinationTerminalsByOrigin(parada.getId_parada());
	}

	public boolean lineExists(long linenumber) 
	{
		return TenantContext.LineaRepository.lineExists(linenumber);
	}

	public List<Pasaje> GetUserTickets(Usuario currentUser) 
	{
		return TenantContext.PasajeRepository.GetByClient(currentUser.getIdUsuario());
	}
	
	public List<Pasaje> GetTicketsByStatusAndTime(TicketStatus status, Date date) 
	{
		return TenantContext.PasajeRepository.GetByStatusAndTime(status, date);
	}

	public List<Asiento> getSeats(int id_viaje, int id_linea, int origen, int destino, long id_vehiculo) 
	{
		List<Asiento> asientos = new ArrayList<>();
		asientos = TenantContext.AsientoRepository.getByVehiculo(id_vehiculo);		
		List<Long> reservados = new ArrayList<>();
		reservados = TenantContext.PasajeRepository.getListaReservados(origen, destino, id_linea, id_viaje);
		for(int x = 0; x < reservados.size(); x++)
		{
			Asiento asiento = getByIdAsiento(asientos, reservados.get(x));
			asiento.setReservado(true);
		}
		return asientos;
	}
	
	public Asiento getByIdAsiento(List<Asiento> asientos, long id_asiento)
	{
		for(int x = 0; x < asientos.size(); x++)
		{
			if(asientos.get(x).getId_asiento() == id_asiento)
			{
				return asientos.get(x); 
			}
		}
		return null;		
	}

	public Double getTicketValue(int origen, int destino, int id_linea) 
	{
		return TenantContext.PasajeRepository.getValorPasaje(origen, destino, id_linea);
	}

	public void ClientConfirmTickets(List<Pasaje> tickets, String paymentId) 
	{
		TenantContext.LineaRepository.ClientConfirmTickets(tickets, paymentId);
	}
	
	public void SalesConfirmTicket(Pasaje ticket) 
	{
		List<Pasaje> tickets = new ArrayList<>();
		tickets.add(ticket);
		TenantContext.LineaRepository.ClientConfirmTickets(tickets, null);
	}
	
	public List<Pasaje> ClientReserveTickets(Usuario currentUser, long id_viaje, int origen, int destino, Double valor,List<Long> reservados) 
	{
		List<Pasaje> tickets = new ArrayList<>();
		Parada parada_baja = TenantContext.ParadaRepository.findByID(destino);
		Parada parada_sube = TenantContext.ParadaRepository.findByID(origen);
		Viaje viaje = TenantContext.ViajeRepository.FindByID(id_viaje);
		for(int x = 0; x < reservados.size(); x++)
		{
			Pasaje ticketToPersist = new Pasaje();
			Asiento asiento = TenantContext.AsientoRepository.getByID(reservados.get(x));
			ticketToPersist.setAsiento(asiento);
			ticketToPersist.setCosto(valor);
			ticketToPersist.setEstado(TicketStatus.Reserved.getValue());			
			ticketToPersist.setParada_baja(parada_baja);			
			ticketToPersist.setParada_sube(parada_sube);
			ticketToPersist.setUser_compra(currentUser);
			ticketToPersist.setUsr_crea(currentUser);
			ticketToPersist.setViaje(viaje);
			UUID auxNum = UUID.randomUUID();
			ticketToPersist.setNumero(auxNum.toString());
			ticketToPersist.setId_pasaje(0);
			TenantContext.LineaRepository.InsertTicket(ticketToPersist);
			tickets.add(ticketToPersist);
		}
		return tickets;
	}
	
	public List<Pasaje> SalesBuyTicketsFromUser(Usuario compraUser, Usuario createUser, long id_viaje, int origen, int destino, Double valor,List<Long> reservados) 
	{
		List<Pasaje> tickets = new ArrayList<>();
		Parada parada_baja = TenantContext.ParadaRepository.findByID(destino);
		Parada parada_sube = TenantContext.ParadaRepository.findByID(origen);
		Viaje viaje = TenantContext.ViajeRepository.FindByID(id_viaje);
		for(int x = 0; x < reservados.size(); x++)
		{
			Pasaje ticketToPersist = new Pasaje();
			Asiento asiento = TenantContext.AsientoRepository.getByID(reservados.get(x));
			ticketToPersist.setAsiento(asiento);
			ticketToPersist.setCosto(valor);
			ticketToPersist.setEstado(TicketStatus.Bought.getValue());			
			ticketToPersist.setParada_baja(parada_baja);			
			ticketToPersist.setParada_sube(parada_sube);
			ticketToPersist.setUser_compra(compraUser);
			ticketToPersist.setUsr_crea(createUser);
			ticketToPersist.setViaje(viaje);
			UUID auxNum = UUID.randomUUID();
			ticketToPersist.setNumero(auxNum.toString());
			ticketToPersist.setId_pasaje(0);
			TenantContext.LineaRepository.InsertTicket(ticketToPersist);
			tickets.add(ticketToPersist);
		}
		return tickets;
	}
	
	public List<Pasaje> SalesBuyTicketsFromCI(String ci_receptor, Usuario createUser, long id_viaje, int origen, int destino, Double valor,List<Long> reservados) 
	{
		List<Pasaje> tickets = new ArrayList<>();
		Parada parada_baja = TenantContext.ParadaRepository.findByID(destino);
		Parada parada_sube = TenantContext.ParadaRepository.findByID(origen);
		Viaje viaje = TenantContext.ViajeRepository.FindByID(id_viaje);
		for(int x = 0; x < reservados.size(); x++)
		{
			Pasaje ticketToPersist = new Pasaje();
			Asiento asiento = TenantContext.AsientoRepository.getByID(reservados.get(x));
			ticketToPersist.setAsiento(asiento);
			ticketToPersist.setCosto(valor);
			ticketToPersist.setEstado(TicketStatus.Bought.getValue());			
			ticketToPersist.setParada_baja(parada_baja);			
			ticketToPersist.setParada_sube(parada_sube);
			//ticketToPersist.setUser_compra(currentUser);
			ticketToPersist.setCi_receptor(ci_receptor);
			ticketToPersist.setUsr_crea(createUser);
			ticketToPersist.setViaje(viaje);
			UUID auxNum = UUID.randomUUID();
			ticketToPersist.setNumero(auxNum.toString());
			ticketToPersist.setId_pasaje(0);
			TenantContext.LineaRepository.InsertTicket(ticketToPersist);
			tickets.add(ticketToPersist);
		}
		return tickets;
	}
	
	public List<Pasaje> DriverBuyTickets(Usuario driver, long id_viaje, int origen, int destino, Double valor,List<Long> reservados) 
	{
		List<Pasaje> tickets = new ArrayList<>();
		Parada parada_baja = TenantContext.ParadaRepository.findByID(destino);
		Parada parada_sube = TenantContext.ParadaRepository.findByID(origen);
		Viaje viaje = TenantContext.ViajeRepository.FindByID(id_viaje);
		for(int x = 0; x < reservados.size(); x++)
		{
			Pasaje ticketToPersist = new Pasaje();
			Asiento asiento = TenantContext.AsientoRepository.getByID(reservados.get(x));
			ticketToPersist.setAsiento(asiento);
			ticketToPersist.setCosto(valor);
			ticketToPersist.setEstado(TicketStatus.cashed.getValue());			
			ticketToPersist.setParada_baja(parada_baja);			
			ticketToPersist.setParada_sube(parada_sube);
			ticketToPersist.setUsr_crea(driver);
			ticketToPersist.setViaje(viaje);
			UUID auxNum = UUID.randomUUID();
			ticketToPersist.setNumero(auxNum.toString());
			ticketToPersist.setId_pasaje(0);
			TenantContext.LineaRepository.InsertTicket(ticketToPersist);
			tickets.add(ticketToPersist);
		}
		return tickets;
	}

	public void DeleteTicket(Pasaje ticket) 
	{
		TenantContext.PasajeRepository.deleteTicket(ticket);
	}	

	public Viaje GetLastTravelByDriver(Usuario user)
	{
		return TenantContext.ViajeRepository.GetLastByDriver(user);
	}

	public void StartTravel(long travelId) 
	{
		TenantContext.PasajeRepository.updateByTravel(travelId, TicketStatus.InTravel);
		TenantContext.EncomiendaRepository.updateByTravel(travelId, PackageStatus.Carring);
	}

	public void CollectTicket(long travelId, String ticketNumber) throws CollectTicketException 
	{
		Pasaje ticket = TenantContext.PasajeRepository.GetByNumber(ticketNumber);
		if(ticket == null)
		{
			throw new CollectTicketException("El boleto es inválido.");
		}
		else
		{
			if(ticket.getViaje().getId_viaje() != travelId)
			{
				throw new CollectTicketException(String.format("El boleto corresponde a otro viaje. Linea %s, Coche Nº %s", 
						ticket.getViaje().getLinea().getNumero(),
						ticket.getViaje().getVehiculo().getId_vehiculo()
				));
			}
			else if(ticket.getEstado() == TicketStatus.Reserved.getValue())
			{
				throw new CollectTicketException("El boleto no fue abonado.");
			}
			else if(ticket.getEstado() == TicketStatus.cashed.getValue())
			{
				throw new CollectTicketException("El boleto ya fue utilizado anteriormente.");
			}
			else
			{
				TenantContext.PasajeRepository.Collect(ticket);
			}
		}
	}
	
	public List<Parada> FindNextStationsByOrigin(long id_parada, long id_linea)
	{
		return TenantContext.ParadaRepository.findNextStationsByOrigin(id_parada, id_linea);
	}

	public Linea GetById(int id_linea)
	{
		return TenantContext.LineaRepository.findByID(id_linea);
	}
	
	public double GetTravelCost(long origin, long destination, long lineId)
	{
		return TenantContext.PasajeRepository.getValorPasaje(origin, destination, lineId);
	}

	public List<Pasaje> GetActiveTickets(Date from, Date to) 
	{
		return TenantContext.PasajeRepository.GetActive(from, to);
	}

	public void CancelTicket(Pasaje ticket) 
	{
		if(ticket.getPaymentId() != null)
		{
			Devolucion refund = new Devolucion();
			refund.setCi_cliente(ticket.getCi_receptor());
			refund.setId_pago(ticket.getPaymentId());
			refund.setRealizado(false);
			
			TenantContext.PasajeRepository.AddRefund(refund);
		}
		DeleteTicket(ticket);
	}

	public void FinishTravel(long travelId) 
	{
		TenantContext.PasajeRepository.updateByTravel(travelId, TicketStatus.cashed);
		TenantContext.EncomiendaRepository.updateByTravel(travelId, PackageStatus.Transported);
		TenantContext.ViajeRepository.finish(travelId);
	}
}
