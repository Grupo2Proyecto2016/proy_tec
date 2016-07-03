package com.springmvc.logic.interfaces;

import java.util.Calendar;
import java.util.List;

import com.springmvc.entities.tenant.Mantenimiento;

public interface IMantenimientoLogic {
	
	public void findVehiculo(long id_vehiculo);
	
	public void findTaller(long id_taller);
	
	public void createMantenimiento(Mantenimiento mantenimiento, Calendar inicio, Calendar fin);
	
	public List<Mantenimiento> getMantenimientos();
	
	public List<Mantenimiento> findServiceByDate(long id_vehiculo, Calendar inicioViaje, Calendar finViaje);
	
	public void deleteMantenimiento(Mantenimiento mantenimiento);
	
}
