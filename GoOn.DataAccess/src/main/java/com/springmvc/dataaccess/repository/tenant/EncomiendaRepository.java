package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class EncomiendaRepository {
	
	EntityManager entityManager;
	
	public EncomiendaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
