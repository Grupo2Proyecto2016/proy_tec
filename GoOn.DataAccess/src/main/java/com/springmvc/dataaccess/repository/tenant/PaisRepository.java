package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class PaisRepository {

	EntityManager entityManager;
	
	public PaisRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}


}
