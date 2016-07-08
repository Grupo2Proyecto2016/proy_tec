package com.springmvc.logic.implementations;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.enums.DayOfWeek;
import com.springmvc.exceptions.BusTravelConcurrencyException;
import com.springmvc.logic.interfaces.IMantenimientoLogic;
import com.springmvc.logic.interfaces.ILinesLogic;

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
	
	public int createMantenimiento(Mantenimiento mantenimiento, Calendar inicio, Calendar fin) throws BusTravelConcurrencyException{
		
			int deli = 0;
		
			LinesLogic ll = new LinesLogic(TenantContext.TenantName);
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			List<Viaje> travels = ll.GetBusTravels(mantenimiento.getVehiculo().getId_vehiculo(), inicio, fin);
			if(!travels.isEmpty())
			{
				throw new BusTravelConcurrencyException("El ómnibus ya posee viajes en período seleccionado");
//				throw new BusTravelConcurrencyException("El ómnibus ya posee viajes entre " + df.format(inicio.getTime())  + " y " + df.format(fin.getTime()));
			}
		
			else
			{
			
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
	    	mantenimientoToPersist.setFacturaContent(null);

			

			TenantContext.MantenimientoRepository.InsertMantenimiento(mantenimientoToPersist);
			deli = 1;
			}
			return deli;
	}
	
	public List<Mantenimiento> getMantenimientos() {
		
		return TenantContext.MantenimientoRepository.getMantenimientos();
	}
	
	public List<Mantenimiento> findServiceByDate(long id_vehiculo, Calendar inicioViaje, Calendar finViaje) {
		
		Calendar tmpinicioViaje = Calendar.getInstance();
		tmpinicioViaje.setTime(inicioViaje.getTime());
		Calendar tmpfinViaje = finViaje;
		tmpinicioViaje.set(Calendar.MILLISECOND, 0);
		tmpinicioViaje.set(Calendar.HOUR , 0);
		tmpinicioViaje.set(Calendar.MINUTE , 0);
		tmpinicioViaje.set(Calendar.SECOND , 0);
		
		Date inicio = tmpinicioViaje.getTime();
		Date fin = tmpfinViaje.getTime();
		
		return TenantContext.MantenimientoRepository.findServiceByDate(id_vehiculo, inicio, fin);
	}
	
	public void deleteMantenimiento(Mantenimiento mantenimiento) 
	{
		if(mantenimiento.factura != null)
		{
			mantenimiento.setFacturaContent(mantenimiento.getFacturaContent());
		}
		TenantContext.MantenimientoRepository.deleteMantenimiento(mantenimiento);
	}
}
