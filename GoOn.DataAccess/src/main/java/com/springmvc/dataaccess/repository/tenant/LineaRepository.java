package com.springmvc.dataaccess.repository.tenant;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Viaje;

public class LineaRepository {
	
	EntityManager entityManager;
	
	public LineaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void InsertLine(Linea linea)
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(linea);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public Linea findByID(long id_linea)
	{
		Linea linea = null;
		Query q = entityManager.createQuery("FROM Linea WHERE id_linea = :idl");
		q.setParameter("idl", id_linea);
		try
		{
			linea = (Linea)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return linea;
	}

	public List<Linea> getLineas() 
	{
		List<Linea> lineas = null;
		Query q = entityManager.createQuery("FROM Linea where habilitado = true");
		lineas = (List<Linea>)q.getResultList();
		return lineas;		
	}

	public boolean TieneViajes(long id_linea) 
	{	
		Query q = entityManager.createQuery("select max(id_viaje) from Viaje where linea_id_linea = :idl");
		q.setParameter("idl", id_linea);
		long maxID;
		try
		{
			maxID = (long) q.getSingleResult();
		} 
		catch(NoResultException ex) 
		{
			return false;
		}
		catch(NullPointerException ex) 
		{
			return false;
		}
		return (maxID>0);
	}

	public void deleteLinea(long id_linea) 
	{	
		EntityTransaction t = entityManager.getTransaction();
		Linea linea = findByID(id_linea);				
		try
		{
			t.begin();
			linea.setHabilitado(false);
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
}
