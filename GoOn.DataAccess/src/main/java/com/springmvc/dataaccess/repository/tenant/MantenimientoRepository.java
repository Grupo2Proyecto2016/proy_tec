package com.springmvc.dataaccess.repository.tenant;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Taller;

public class MantenimientoRepository {
	
	EntityManager entityManager;
	
	public MantenimientoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	
	public List<Mantenimiento> getMantenimientos() 
	{
		List<Mantenimiento> mantenimientos = null;
		Query q = entityManager.createQuery("FROM Mantenimiento");
		mantenimientos = (List<Mantenimiento>)q.getResultList();
		return mantenimientos;	
	}
	
	public void InsertMantenimiento(Mantenimiento mantenimiento)
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			entityManager.persist(mantenimiento);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	
	public Mantenimiento FindByID(long id_mantenimiento)
	{
		Mantenimiento mantenimiento = null;
		Query q = entityManager.createQuery("FROM Mantenimiento WHERE id_mantenimiento = :idm");
		q.setParameter("idm", id_mantenimiento);
		try
		{
			mantenimiento = (Mantenimiento)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return mantenimiento;
	}

	public void deleteMantenimiento(long id_mantenimiento)
	{
		
		Mantenimiento mant = FindByID(id_mantenimiento);
		
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			mant.setEstado(0);
			mant.setFecha(new Date());
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
		
		
		
	}
	
}
