package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class VehiculoRepository {
	
EntityManager entityManager;
	
	public VehiculoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
