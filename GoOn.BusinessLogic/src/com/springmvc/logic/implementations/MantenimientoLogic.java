package com.springmvc.logic.implementations;


import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.logic.interfaces.IMantenimientoLogic;
import com.springmvc.requestWrappers.MantenimientoFormWrapper;

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
	
	public void createMantenimiento(MantenimientoFormWrapper mantenimiento) {
		mantenimiento.setCosto(null);
		TenantContext.MantenimientoRepository.InsertMantenimiento(mantenimiento);
	}
	
	public List<Mantenimiento> getMantenimientos() {
		
		return TenantContext.MantenimientoRepository.getMantenimientos();
	}
	
	public void deleteMantenimiento(long id_mantenimiento) 
	{
		TenantContext.MantenimientoRepository.deleteMantenimiento(id_mantenimiento);
	}
}
