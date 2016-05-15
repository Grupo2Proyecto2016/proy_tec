package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class ParadaRepository 
{
	EntityManager entityManager;
	
	public ParadaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
