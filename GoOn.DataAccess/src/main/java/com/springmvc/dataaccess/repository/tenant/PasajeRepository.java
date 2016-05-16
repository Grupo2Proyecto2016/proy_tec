package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class PasajeRepository {
	
	EntityManager entityManager;
	
	public PasajeRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
