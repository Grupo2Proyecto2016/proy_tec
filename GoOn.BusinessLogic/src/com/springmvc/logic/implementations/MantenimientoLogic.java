package com.springmvc.logic.implementations;


import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Usuario;
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
	
	public void createMantenimiento(Mantenimiento mantenimiento) {
		// Cuando el vehículo entra al mantenimiento el estado es "1"
		mantenimiento.setEstado(1);
		mantenimiento.setCosto(null);
		mantenimiento.setFecha(null);
		TenantContext.MantenimientoRepository.InsertMantenimiento(mantenimiento);
		
	}
	
	public List<Mantenimiento> getMantenimientos() {
		
		return TenantContext.MantenimientoRepository.getMantenimientos();
	}
}
