package com.springmvc.logic.implementations;


import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.entities.tenant.Taller;
import com.springmvc.logic.interfaces.IMantenimientoLogic;

public class MantenimientoLogic implements IMantenimientoLogic {

	private TenantDAContext TenantContext;
	
	public MantenimientoLogic(String tenant) {
		
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	public void insertVehiculo(Vehiculo vehiculo){
		
		TenantContext.VehiculoRepository.InsertBus(vehiculo);
	}
	
	public void insertTaller(Taller taller) {
		
		TenantContext.TallerRepository.InsertTaller(taller);
	}
	
	public List<Mantenimiento> GetMantenimientos() {
		
		return TenantContext.MantenimientoRepository.getMantenimientos();
	}
}
