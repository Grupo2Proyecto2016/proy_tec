package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class LineaRepository {
	
	EntityManager entityManager;
	
	public LineaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
