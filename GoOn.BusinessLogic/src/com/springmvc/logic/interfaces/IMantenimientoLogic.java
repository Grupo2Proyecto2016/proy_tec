package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.tenant.Mantenimiento;

public interface IMantenimientoLogic {
	
	public void findVehiculo(long id_vehiculo);
	
	public void findTaller(long id_taller);
	
	public void createMantenimiento(Mantenimiento mantenimiento);
	
	public List<Mantenimiento> GetMantenimientos();
	
}
