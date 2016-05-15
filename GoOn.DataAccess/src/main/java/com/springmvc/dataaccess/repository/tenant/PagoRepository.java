package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class PagoRepository {
	
EntityManager entityManager;
	
	public PagoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
