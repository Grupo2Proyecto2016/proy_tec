package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.main.EntityMapper;
import com.springmvc.entities.tenant.Asiento;
import com.springmvc.entities.tenant.Vehiculo;

public class DevolucionRepository {
	
	EntityManager entityManager;
	
	public DevolucionRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
}
