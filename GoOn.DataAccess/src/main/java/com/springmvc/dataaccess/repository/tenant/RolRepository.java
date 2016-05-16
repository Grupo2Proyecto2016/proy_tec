package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class RolRepository {

	EntityManager entityManager;
	
	public RolRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
}
