package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Taller;
import com.springmvc.entities.tenant.Vehiculo;

public interface IMantenimientoLogic {

	public void insertVehiculo(Vehiculo vehiculo);
	
	public void insertTaller(Taller taller);
	
	public List<Mantenimiento> GetMantenimientos();
	
}
