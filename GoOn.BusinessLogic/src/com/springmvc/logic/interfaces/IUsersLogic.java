package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.main.Usuario;

public interface IUsersLogic {
	com.springmvc.entities.tenant.Usuario GetUserByName(String tenant, String userName);
	
	Usuario GetUserByName(String userName);
	
	Usuario findById(long id);
	
	void saveUser(Usuario user);
	
	void updateUser(Usuario user);
	
	void deleteUserById(long id);

	List<Usuario> findAllUsers(); 
	
	void deleteAllUsers();
	
	boolean isUserExist(String username);
}
