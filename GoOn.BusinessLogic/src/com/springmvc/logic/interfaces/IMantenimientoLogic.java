package com.springmvc.logic.interfaces;

import java.util.Calendar;
import java.util.List;

import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.exceptions.BusTravelConcurrencyException;

public interface IMantenimientoLogic {
	
	public void findVehiculo(long id_vehiculo);
	
	public void findTaller(long id_taller);
	
	public int createMantenimiento(Mantenimiento mantenimiento, Calendar inicio, Calendar fin) throws BusTravelConcurrencyException;
	
	public List<Mantenimiento> getMantenimientos();
	
	public List<Mantenimiento> findServiceByDate(long id_vehiculo, Calendar inicioViaje, Calendar finViaje);
	
	public void deleteMantenimiento(Mantenimiento mantenimiento);
	
}
