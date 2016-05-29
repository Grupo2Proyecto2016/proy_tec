package com.springmvc.logic.implementations;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springmvc.dataaccess.context.MainDAContext;
import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.main.Pais;
import com.springmvc.entities.main.Usuario;
import com.springmvc.entities.tenant.Sucursal;
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

	public void CreateTenant(Empresa company, com.springmvc.entities.tenant.Usuario user, Sucursal sucursal) throws Exception
	{
		try
		{
			context.CreateTenant(company);
			UsersLogic userLogic = new UsersLogic(company.getNombreTenant(), true);
			userLogic.SetUpRoles();
			user.setSucursal(sucursal);
			userLogic.CreateUser(user);
			//BranchesLogic branchLogic = new BranchesLogic(company.getNombreTenant());
			//branchLogic.createBranch(sucursal);
			
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	@Override
	public List<Pais> GetCountries() {
		List<Pais> countries = context.GetCountries();
		return countries;
	}

	@Override
	public Pais GetCountry(long countryId) 
	{
		return context.GetCountry(countryId);
	}
	
	@Override
	public Empresa GetCompany(String tenantid)
	{
		return context.GetCompany(tenantid);
	}
	
	public List<Empresa> GetCompanies()
	{
		return context.GetCompanies();
	}
}
