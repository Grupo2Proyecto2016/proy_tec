package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.springmvc.entities.tenant.Sucursal;


public class SucursalRepository {
	
	EntityManager entityManager;
	
	public SucursalRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public void insertBranch(Sucursal sucursal) 
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			entityManager.persist(sucursal);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
}
