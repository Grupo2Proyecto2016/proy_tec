package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class ConfiguracionGlobalRepository {
	
EntityManager entityManager;
	
	public ConfiguracionGlobalRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
