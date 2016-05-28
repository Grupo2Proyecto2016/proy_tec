package com.springmvc.logic.implementations;

import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Sucursal;

public class BranchesLogic {
	
	private TenantDAContext TenantContext;
	
	public BranchesLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant, false);
	}

	public void createBranch(Sucursal sucursal) 
	{
		TenantContext.SucursalRepository.insertBranch(sucursal);		
	}

	public List<Sucursal> GetBranches() 
	{
		return TenantContext.SucursalRepository.GetBranches();	
	}

	public Sucursal GetBranch(int id) 
	{
		return TenantContext.SucursalRepository.GetBranch(id);
	}
}
