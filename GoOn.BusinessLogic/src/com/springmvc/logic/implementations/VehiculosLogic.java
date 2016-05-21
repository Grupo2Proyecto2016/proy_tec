package com.springmvc.logic.implementations;

import java.util.List;


import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.logic.interfaces.IVehiculosLogic;

public class VehiculosLogic implements IVehiculosLogic{
	
	private TenantDAContext TenantContext;
		
	public VehiculosLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	@Override
	public List<Vehiculo> GetVehiculos() 
	{		
		return TenantContext.VehiculoRepository.getVehiculos();
	}

}
