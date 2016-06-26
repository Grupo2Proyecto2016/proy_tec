package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.springmvc.entities.tenant.Encomienda;

public class EncomiendaRepository {
	
	EntityManager entityManager;
	
	public EncomiendaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public void AddPackage(Encomienda pack) 
	{
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			entityManager.persist(pack);
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
