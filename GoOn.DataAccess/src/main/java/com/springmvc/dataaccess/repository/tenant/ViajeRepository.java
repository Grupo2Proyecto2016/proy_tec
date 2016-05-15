package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class ViajeRepository {

	EntityManager entityManager;
	
	public ViajeRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
}
