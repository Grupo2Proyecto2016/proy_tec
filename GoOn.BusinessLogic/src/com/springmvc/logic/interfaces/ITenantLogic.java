package com.springmvc.logic.interfaces;

import com.springmvc.entities.main.Usuario;

public interface ITenantLogic 
{
	boolean TenantExists(String tenantid);
	
	Usuario GetUserByName(String username);
}
