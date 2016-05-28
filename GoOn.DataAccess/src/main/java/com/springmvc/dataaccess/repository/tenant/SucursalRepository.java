package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Vehiculo;


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

	public List<Sucursal> GetBranches() 
	{
		List<Sucursal> result = new ArrayList<>();
		Query q = entityManager.createQuery("FROM Sucursal");
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

	public Sucursal GetBranch(long id) 
	{
		Sucursal result = null;
		Query q = entityManager.createQuery("FROM Sucursal WHERE id_sucursal = :id");
		q.setParameter("id", id);
		try
		{
			result = (Sucursal)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return result;
	}

	public boolean TieneEmpleados(long id_sucursal) 
	{
		Query q = entityManager.createQuery("select max(id_usuario) from Usuario where id_sucursal = :ids");
		q.setParameter("ids", id_sucursal);
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

	public void deleteBranch(long id_sucursal) 
	{
		EntityTransaction t = entityManager.getTransaction();
		Sucursal sucursal = GetBranch(id_sucursal);
		if (sucursal != null)
		{
			try
			{
				t.begin();
				entityManager.remove(sucursal);
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
}
