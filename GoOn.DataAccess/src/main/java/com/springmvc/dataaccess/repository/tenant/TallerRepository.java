package com.springmvc.dataaccess.repository.tenant;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Taller;
import com.springmvc.entities.tenant.Vehiculo;

public class TallerRepository {
	
	EntityManager entityManager;
	
	public TallerRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public List<Taller> getTalleres() 
	{
		List<Taller> talleres = null;
		Query q = entityManager.createQuery("FROM Taller");
		talleres = (List<Taller>)q.getResultList();
		return talleres;	
	}
	
	public void InsertTaller(Taller taller)
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			entityManager.persist(taller);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public Taller FindByID(long id_taller)
	{
		Taller taller = null;
		Query q = entityManager.createQuery("FROM Taller WHERE id_taller = :idt");
		q.setParameter("idt", id_taller);
		try
		{
			taller = (Taller)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return taller;
	}
}
