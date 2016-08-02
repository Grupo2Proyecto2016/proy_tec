package com.springmvc.dataaccess.repository.tenant;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Vehiculo;

public class VehiculoRepository {
	
EntityManager entityManager;
	
	public VehiculoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public List<Vehiculo> getVehiculos() 
	{
		List<Vehiculo> vehiculos = null;
		Query q = entityManager.createQuery("FROM Vehiculo");
		vehiculos = (List<Vehiculo>)q.getResultList();
		return vehiculos;	
	}
	
	public void InsertBus(Vehiculo vehiculo)
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			entityManager.persist(vehiculo);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public Boolean TieneViajes(long id_vehiculo)
	{
		Query q = entityManager.createQuery("select max(id_viaje) from Viaje where vehiculo_id_vehiculo = :idv");
		q.setParameter("idv", id_vehiculo);
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

	public void deleteBus(long id_vehiculo) 
	{	
		EntityTransaction t = entityManager.getTransaction();
		Vehiculo vehiculo = FindByID(id_vehiculo);
		if (vehiculo != null)
		{
			try
			{
				t.begin();
				entityManager.remove(vehiculo);
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
	
	public Vehiculo FindByID(long id_vehiculo)
	{
		Vehiculo vehiculo = null;
		Query q = entityManager.createQuery("FROM Vehiculo WHERE id_vehiculo = :idv");
		q.setParameter("idv", id_vehiculo);
		try
		{
			vehiculo = (Vehiculo)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return vehiculo;
	}

	public Vehiculo getByNumber(long numerov)
	{
		Vehiculo vehiculo = null;
		Query q = entityManager.createQuery("FROM Vehiculo WHERE numerov = :n");
		q.setParameter("n", numerov);
		try
		{
			vehiculo = (Vehiculo)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return vehiculo;
	}

}
