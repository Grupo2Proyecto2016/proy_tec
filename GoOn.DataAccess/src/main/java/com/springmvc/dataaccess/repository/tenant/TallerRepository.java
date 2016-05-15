package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class TallerRepository {
	
	EntityManager entityManager;
	
	public TallerRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
