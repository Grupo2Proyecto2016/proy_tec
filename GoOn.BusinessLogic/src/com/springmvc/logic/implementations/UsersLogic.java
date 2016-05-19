package com.springmvc.logic.implementations;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springmvc.dataaccess.context.MainDAContext;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.main.Usuario;
import com.springmvc.logic.interfaces.IUsersLogic;

@Component
public class UsersLogic implements IUsersLogic {
	
	private static final AtomicLong counter = new AtomicLong();
	
	private TenantDAContext TenantContext;
	@Autowired MainDAContext dataContext;
	
	public UsersLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant);
	}
	
	public UsersLogic()
	{
	}
	
	public com.springmvc.entities.tenant.Usuario GetUserByName(String userName)
	{
		com.springmvc.entities.tenant.Usuario user = TenantContext.UserRepository.FindByUsername(userName);
    	return user;
	}
	
	public Usuario GetMainUserByName(String userName)
	{
		Usuario user = dataContext.GetUserByName(userName);
    	return user;
	}
	
	public void CreateUser(com.springmvc.entities.tenant.Usuario user)
	{
		TenantContext.UserRepository.InsertUser(user);
	}
}
