package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Parada;

public class EncomiendaRepository {
	
	EntityManager entityManager;
	
	public EncomiendaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public void AddPackage(Encomienda pack) 
	{
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			entityManager.persist(pack);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public List<Encomienda> GetByClient(long idUsuario) 
	{
		List<Encomienda> result = new ArrayList<>();
		Query q = entityManager.createQuery("SELECT e FROM Encomienda e "
				+ "WHERE e.usr_envia.id_usuario = :idUsuario OR e.usr_recibe.id_usuario = :idUsuario "
				+ "ORDER BY e.status ASC, e.viaje.inicio DESC "
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
