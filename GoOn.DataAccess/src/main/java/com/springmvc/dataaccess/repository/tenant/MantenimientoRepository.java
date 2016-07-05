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
		try {
			mantenimientos = (List<Mantenimiento>)q.getResultList();
			return mantenimientos;
		} catch (Exception e) {
			throw e;
		}
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
	
	public List<Mantenimiento> findServiceByDate(long id_vehiculo, Date inicioViaje, Date finViaje)
	{
		List<Mantenimiento> mantenimientos = null;
		Query q = entityManager.createQuery("FROM Mantenimiento WHERE vehiculo_id_vehiculo = :idv "
				+ "AND id_mantenimiento NOT IN "
				+ "(SELECT id_mantenimiento FROM Mantenimiento WHERE "
				+ "(fin < :vi AND fin < :vf) "
				+ "OR (:vf < inicio AND :vf < fin))");
		q.setParameter("idv", id_vehiculo);
		q.setParameter("vi", inicioViaje);
		q.setParameter("vf", finViaje);
		try {
			mantenimientos = (List<Mantenimiento>)q.getResultList();
			return mantenimientos;
		}
		catch (NoResultException ex) {
			return null;
		}	
	}

	public void deleteMantenimiento(Mantenimiento mantenimiento)
	{
		
		Mantenimiento mant = FindByID(mantenimiento.getId_mantenimiento());
		
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			mant.setCosto(mantenimiento.getCosto());
			mant.setFacturaContent(mantenimiento.getFacturaContent());
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
		
		
		
	}
	
}
