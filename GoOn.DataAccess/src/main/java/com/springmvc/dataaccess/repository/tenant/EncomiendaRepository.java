package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.enums.PackageStatus;

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

	public List<Encomienda> GetBranchPackages(Parada terminal, Date from, Date to) 
	{
		List<Encomienda> result = new ArrayList<>();
		Query q = entityManager.createQuery("SELECT e FROM Encomienda e "
				+ "WHERE e.viaje.inicio >= :from AND e.viaje.inicio <= :to "
				+ "AND (e.viaje.linea.origen.id_parada = :idt OR e.viaje.linea.destino.id_parada = :idt) "
				+ "ORDER BY e.status DESC, e.viaje.inicio ASC "
		);
		q.setParameter("idt", terminal.getId_parada());
		q.setParameter("from", from);
		q.setParameter("to", to);
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

	public Encomienda GetById(long id_encomienda) 
	{
		Encomienda pack;
		Query q = entityManager.createQuery("FROM Encomienda e WHERE e.id_encomienda = :idp");
		q.setParameter("idp", id_encomienda);
		try
		{
			pack = (Encomienda)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return pack;
	}
	
	public void DeliverPackage(long id_encomienda) 
	{
		Encomienda pack = GetById(id_encomienda);
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			pack.setStatus(PackageStatus.Delivered.getValue());
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

}
