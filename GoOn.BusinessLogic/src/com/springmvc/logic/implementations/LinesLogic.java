package com.springmvc.logic.implementations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.entities.tenant.Viaje;
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
		tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 1);;
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
		
		// TODO Auto-generated method stub
		return null;
	}

	public List<Parada> GetStationsByDestinations(List<Integer> destinations) 
	{		
		
		List<Linea> lineas = TenantContext.LineaRepository.getLineasbyIdDestinations(destinations); //devuelve una lista de lineas (distinct) que pasan por los puntos enviados
		List<Parada> stations = new ArrayList<Parada>();
		for(int i = 0; i < lineas.size(); i++) //recorre las lineas y si la parada pertenece, tira todas las anteriores
		{
			boolean pertenece = false;
			for(int x = 0; x < destinations.size(); x++)
			{
				if(paradaPerteneceALinea(destinations.get(x).longValue(), lineas.get(i)))
				{
					List<Parada> paradas = lineas.get(i).getParadas();
					boolean sigo = true;
					for(int y = 0; y < paradas.size(); y++)
					{						
						if(paradas.get(y).getId_parada() != destinations.get(x).longValue())
						{
							if(sigo)
							{
								stations.add(paradas.get(y));
							}
						}
						if(paradas.get(y).getId_parada() == destinations.get(x).longValue())
						{
							sigo = false;
						}
					}
				}
			}			
		}
		return stations;
	}
	
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
}
