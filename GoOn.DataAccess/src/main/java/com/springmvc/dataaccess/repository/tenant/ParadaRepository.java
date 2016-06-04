package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;

public class ParadaRepository 
{
	EntityManager entityManager;
	
	public ParadaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public List<Parada> GetTerminals() 
	{
		List<Parada> result = new ArrayList<>();
		Query q = entityManager.createQuery("FROM Parada WHERE es_terminal = TRUE");
		try
		{
			result = q.getResultList();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return result;
	}

	public void InsertStation(Parada terminal) 
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			entityManager.persist(terminal);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public Parada findByID(long id_parada)
	{
		Parada parada = null;
		Query q = entityManager.createQuery("FROM Parada WHERE id_parada = :idp");
		q.setParameter("idp", id_parada);
		try
		{
			parada = (Parada)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return parada;
	}

}
