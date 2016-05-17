package com.springmvc.logic.implementations;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springmvc.dataaccess.context.MainDAContext;
import com.springmvc.entities.main.Pais;
import com.springmvc.entities.main.Usuario;
import com.springmvc.logic.interfaces.ITenantLogic;

@Component
public class TenantLogic implements ITenantLogic {
	
	@Autowired MainDAContext context;
	
	public boolean TenantExists(String tenantid)
	{
		String tenant = context.GetTenant(tenantid); 
		return tenant != null;
	}
	
	public Usuario GetUserByName(String username)
	{
		Usuario user = context.GetUserByName(username);
		return user;
	}

	@Override
	public List<Pais> GetCountries() {
		List<Pais> countries = context.GetCountries();
		return countries;
	}
}
