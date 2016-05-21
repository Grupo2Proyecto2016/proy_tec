package com.springmvc.logic.interfaces;

public interface IUsersLogic {

	com.springmvc.entities.tenant.Usuario GetUserByName(String userName);
	
	void CreateUser(com.springmvc.entities.tenant.Usuario user);
	
	com.springmvc.entities.tenant.Usuario GetTenantAdmin();
}
