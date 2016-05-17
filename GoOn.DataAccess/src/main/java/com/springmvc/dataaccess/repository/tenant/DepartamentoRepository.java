package com.springmvc.dataaccess.repository.tenant;

import javax.persistence.EntityManager;

public class DepartamentoRepository {

	EntityManager entityManager;
	
	public DepartamentoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

}
