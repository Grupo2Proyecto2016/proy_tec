package com.springmvc.logic.implementations;

import com.springmvc.logic.interfaces.ITallerLogic;

import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Taller;

public class TallerLogic implements ITallerLogic {
	
	
	private TenantDAContext TenantContext;

	public TallerLogic(String tenant) {
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	public void createTaller(Taller taller){
	
		TenantContext.TallerRepository.InsertTaller(taller);
	}
	
	public List<Taller> getTalleres() {
		
		return TenantContext.TallerRepository.getTalleres();
	}
}
