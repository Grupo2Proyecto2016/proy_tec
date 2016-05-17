package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class CiudadRepository {
	
	EntityManager entityManager;
	
	public CiudadRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
