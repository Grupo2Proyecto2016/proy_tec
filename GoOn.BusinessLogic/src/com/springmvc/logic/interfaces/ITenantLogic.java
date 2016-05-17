package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.main.Pais;
import com.springmvc.entities.main.Usuario;

public interface ITenantLogic 
{
	boolean TenantExists(String tenantid);
	
	Usuario GetUserByName(String username);
	List<Pais> GetCountries();
}
