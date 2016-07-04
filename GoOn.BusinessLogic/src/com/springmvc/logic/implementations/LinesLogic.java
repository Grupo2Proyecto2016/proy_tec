package com.springmvc.logic.implementations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Asiento;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.entities.tenant.ViajesBuscados;
import com.springmvc.enums.DayOfWeek;
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

	public void CreateTravels(Viaje travel, Map<DayOfWeek, Boolean> days, Calendar dayFrom, Calendar dayTo, Date time) 
	{
		boolean dayInPeriod = true;
		while (dayInPeriod) 
		{
			int dayOfWeek = dayFrom.get((GregorianCalendar.DAY_OF_WEEK));
			if(days.get(DayOfWeek.values()[dayOfWeek - 1]))
			{
				dayFrom.getTimeZone();
				Date travelDate = dayFrom.getTime();
				travelDate.setHours(time.getHours());
				travelDate.setMinutes(time.getMinutes());
				
				Viaje travelToPersist = new Viaje();
				travelToPersist.setConductor(travel.getConductor());
				travelToPersist.setLinea(travel.getLinea());
				travelToPersist.setVehiculo(travel.getVehiculo());
				travelToPersist.setInicio(travelDate);
				TenantContext.ViajeRepository.InsertTravel(travelToPersist);
			}
			dayFrom.add((Calendar.DATE), 1);
			dayFrom.set(Calendar.MILLISECOND, 0);
			dayFrom.set(Calendar.HOUR , 0);
			dayFrom.set(Calendar.MINUTE , 0);
			dayFrom.set(Calendar.SECOND , 0);
			dayInPeriod = !dayTo.before(dayFrom);
		}
	}

	public List<Viaje> GetTravels() 
	{
		return TenantContext.ViajeRepository.GetTravels();
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

	public List<Asiento> getSeats(int id_viaje, int id_linea, int origen, int destino, long id_vehiculo) 
	{
		List<Asiento> asientos = new ArrayList<>();
		asientos = TenantContext.AsientoRepository.getByVehiculo(id_vehiculo);
		List<Long> reservados = new ArrayList<>();
		reservados = TenantContext.PasajeRepository.getListaReservados(origen, destino, id_linea, id_viaje);
		//ver los reservados y marcarlos
		return asientos;
	}
}
