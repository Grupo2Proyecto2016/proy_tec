package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.exceptions.UserAlreadyExistsException;

public interface IUsersLogic {

	com.springmvc.entities.tenant.Usuario GetUserByName(String userName);
	
	void CreateUser(com.springmvc.entities.tenant.Usuario user)throws UserAlreadyExistsException;
	
	com.springmvc.entities.tenant.Usuario GetTenantAdmin();
	
	List<com.springmvc.entities.tenant.Usuario> GetEmployees();
}
