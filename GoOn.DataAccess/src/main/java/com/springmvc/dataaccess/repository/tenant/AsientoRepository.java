package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.main.EntityMapper;
import com.springmvc.entities.tenant.Asiento;

public class AsientoRepository {
	
	EntityManager entityManager;
	
	public AsientoRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public List<Asiento> getByVehiculo(long id_vehiculo) 
	{
		List<Asiento> result = new ArrayList<>();
		Query q = entityManager.createNativeQuery("SELECT id_asiento, numero, es_ventana, es_accesible, habilitado from Asiento a WHERE a.id_vehiculo = :idVehiculo");
		q.setParameter("idVehiculo", id_vehiculo);
		try
		{
			result = EntityMapper.getResultList(q, Asiento.class); //para evitar poner vehiculo en asiento, si lo necesitamos mas adelante se saca
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return result;	
	}

}
