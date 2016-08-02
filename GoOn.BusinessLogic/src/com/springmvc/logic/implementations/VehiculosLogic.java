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

	public void createBus(Vehiculo vehiculo) 
	{
		TenantContext.VehiculoRepository.InsertBus(vehiculo);
	}
	
	public Boolean TieneViajes(long id_vehiculo)
	{
		return TenantContext.VehiculoRepository.TieneViajes(id_vehiculo);
	}

	public void deleteBus(long id_vehiculo) 
	{
		TenantContext.VehiculoRepository.deleteBus(id_vehiculo);
	}

	public Vehiculo GetBusByNumber(long numerov)
	{
		return TenantContext.VehiculoRepository.getByNumber(numerov);
	}

}
