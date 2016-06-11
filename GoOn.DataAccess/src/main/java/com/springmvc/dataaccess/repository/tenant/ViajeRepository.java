package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.entities.tenant.Viaje;

public class ViajeRepository {

	EntityManager entityManager;
	
	public ViajeRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public Viaje FindByID(long travelId)
	{
		Viaje travel = null;
		Query q = entityManager.createQuery("FROM Viaje WHERE id_viaje = :idv");
		q.setParameter("idv", travelId);
		try
		{
			travel = (Viaje)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return travel;
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

	public List<Viaje> GetTravels() 
	{
		List<Viaje> travels = null;
		Query q = entityManager.createQuery("FROM Viaje");
		travels = (List<Viaje>)q.getResultList();
		return travels;	
	}

	public void DeleteTravel(long travelId) 
	{
		EntityTransaction t = entityManager.getTransaction();
		Viaje travel = FindByID(travelId);				
		try
		{
			t.begin();
			entityManager.remove(travel);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public boolean HasPackages(long travelId) 
	{
		Query q = entityManager.createQuery("select max(id_encomienda) from Encomienda where id_viaje = :idv");
		q.setParameter("idv", travelId);
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
		return (maxID > 0);
	}

	public boolean HasTickets(long travelId) 
	{
		Query q = entityManager.createQuery("select max(id_pasaje) from Pasaje where viaje_id_viaje = :idv");
		q.setParameter("idv", travelId);
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
		return (maxID > 0);
	}
}
