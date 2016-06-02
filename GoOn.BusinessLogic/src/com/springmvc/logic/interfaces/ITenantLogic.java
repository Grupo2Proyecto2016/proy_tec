package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.main.Pais;
import com.springmvc.entities.main.Usuario;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Sucursal;

public interface ITenantLogic 
{
	boolean TenantExists(String tenantid);
	
	Usuario GetUserByName(String username);
	
	List<Pais> GetCountries();
	
	void CreateTenant(Empresa company, com.springmvc.entities.tenant.Usuario user, Sucursal sucursal, Parada terminal)throws Exception;

	Pais GetCountry(long countryId);

	Empresa GetCompany(String tenantid);
	
	List<Empresa> GetCompanies();
	
	void UpdateCompany(String tenantid, Empresa companyUpdateData);
}
