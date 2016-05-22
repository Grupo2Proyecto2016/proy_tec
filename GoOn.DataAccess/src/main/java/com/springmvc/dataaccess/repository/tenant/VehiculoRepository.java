package com.springmvc.dataaccess.repository.tenant;

import java.util.List;

import javax.persistence.EntityManager;
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
		try
		{
			entityManager.getTransaction().begin();
			entityManager.persist(vehiculo);
			entityManager.flush();
			entityManager.getTransaction().commit();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

}
