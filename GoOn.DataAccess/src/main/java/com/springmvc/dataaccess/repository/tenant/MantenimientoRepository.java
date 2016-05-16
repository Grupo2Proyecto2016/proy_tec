package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class MantenimientoRepository {
	EntityManager entityManager;
	
	public MantenimientoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
