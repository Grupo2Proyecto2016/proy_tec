package com.springmvc.logic.implementations;

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
}
