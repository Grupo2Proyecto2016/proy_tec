package com.springmvc.logic.implementations;

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
}
