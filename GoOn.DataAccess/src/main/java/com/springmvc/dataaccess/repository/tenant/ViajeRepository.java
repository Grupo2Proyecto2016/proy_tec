package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.springmvc.entities.tenant.Viaje;

public class ViajeRepository {

	EntityManager entityManager;
	
	public ViajeRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void InsertTravel(Viaje travel) 
	{
		travel.setEncomiendas(new ArrayList<>());
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(travel);
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
