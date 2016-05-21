package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.web.portlet.handler.UserRoleAuthorizationInterceptor;

import com.springmvc.entities.tenant.Rol;
import com.springmvc.enums.UserRol;

public class RolRepository {

	EntityManager entityManager;
	
	public RolRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public void SetUp() 
	{
		try
		{
			List<Rol> roles = new ArrayList<>();
			roles.add(UserRol.Admin.ToRol());
			roles.add(UserRol.Sales.ToRol());
			roles.add(UserRol.Driver.ToRol());
			roles.add(UserRol.Client.ToRol());
			
			entityManager.getTransaction().begin();

			for (Rol rol : roles) 
			{
				entityManager.persist(rol);
			}
			entityManager.flush();
			entityManager.getTransaction().commit();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}
}
