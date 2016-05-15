package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class AsientoRepository {
	
	EntityManager entityManager;
	
	public AsientoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
