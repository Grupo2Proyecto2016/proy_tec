package com.springmvc.dataaccess.repository.tenant;

import java.util.List;

import javax.persistence.EntityManager;
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
		Query q = entityManager.createQuery("FROM Taller");
		mantenimientos = (List<Mantenimiento>)q.getResultList();
		return mantenimientos;	
	}
}
