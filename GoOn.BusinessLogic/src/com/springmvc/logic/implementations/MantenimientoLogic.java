package com.springmvc.logic.implementations;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.enums.DayOfWeek;
import com.springmvc.logic.interfaces.IMantenimientoLogic;

public class MantenimientoLogic implements IMantenimientoLogic {

	private TenantDAContext TenantContext;
	
	public MantenimientoLogic(String tenant) {
		
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	public void findVehiculo(long id_vehiculo){
		
		TenantContext.VehiculoRepository.FindByID(id_vehiculo);
	}
	
	public void findTaller(long id_taller) {
		
		TenantContext.TallerRepository.FindByID(id_taller);
	}
	
	public void createMantenimiento(Mantenimiento mantenimiento, Calendar inicio, Calendar fin) {
		
		
			inicio.getTimeZone();
			fin.getTimeZone();
			
			inicio.add((Calendar.DATE), 1);
			inicio.set(Calendar.MILLISECOND, 0);
			inicio.set(Calendar.HOUR , 0);
			inicio.set(Calendar.MINUTE , 0);
			inicio.set(Calendar.SECOND , 0);
			
						
			Date mantenimientoInicio = inicio.getTime();
			Date mantenimientoFin = fin.getTime();

			
			
			Mantenimiento mantenimientoToPersist = new Mantenimiento();
	    	mantenimientoToPersist.setCosto(null);
	    	mantenimientoToPersist.setTaller(mantenimiento.getTaller());
	    	mantenimientoToPersist.setVehiculo(mantenimiento.getVehiculo());
	    	mantenimientoToPersist.setInicio(mantenimientoInicio);
	    	mantenimientoToPersist.setFin(mantenimientoFin);
	    	mantenimientoToPersist.setUser_crea(mantenimiento.getUser_crea());
			TenantContext.MantenimientoRepository.InsertMantenimiento(mantenimientoToPersist);

	}
	
	public List<Mantenimiento> getMantenimientos() {
		
		return TenantContext.MantenimientoRepository.getMantenimientos();
	}
	
	public void deleteMantenimiento(Mantenimiento mantenimiento) 
	{
		TenantContext.MantenimientoRepository.deleteMantenimiento(mantenimiento);
	}
}
