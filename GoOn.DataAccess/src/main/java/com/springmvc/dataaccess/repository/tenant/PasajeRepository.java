package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Pasaje;

public class PasajeRepository {
	
	EntityManager entityManager;
	
	public PasajeRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public List<Pasaje> GetByClient(long idUsuario)
	{
		List<Pasaje> result = new ArrayList<>();
		Query q = entityManager.createQuery("SELECT p FROM Pasaje p "
				+ "WHERE p.user_compra.id_usuario = :idUsuario "
				+ "ORDER BY p.estado ASC, p.viaje.inicio DESC "
		);
		q.setParameter("idUsuario", idUsuario);
		try
		{
			result = q.getResultList();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return result;
	}

}
