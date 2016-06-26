package com.springmvc.dataaccess.repository.tenant;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;

public class ParadaRepository 
{
	EntityManager entityManager;
	
	public ParadaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public List<Parada> GetTerminals() 
	{
		List<Parada> result = new ArrayList<>();
		Query q = entityManager.createQuery("FROM Parada WHERE es_terminal = TRUE");
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

	public void InsertStation(Parada terminal) 
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();
			entityManager.persist(terminal);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public Parada findByID(long id_parada)
	{
		Parada parada = null;
		Query q = entityManager.createQuery("FROM Parada WHERE id_parada = :idp");
		q.setParameter("idp", id_parada);
		try
		{
			parada = (Parada)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return parada;
	}

	public List<Parada> FindAll() 
	{
		List<Parada> result = new ArrayList<>();
		String queryString = "SELECT DISTINCT p FROM Linea l "
				+ "INNER JOIN l.paradas p "
				+ "WHERE l.habilitado = TRUE";
		Query q = entityManager.createQuery(queryString);
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

	public List<Parada> GetPackageTerminals() 
	{
		List<Parada> result = new ArrayList<>();
		Query q = entityManager.createQuery(
			"SELECT DISTINCT s.terminal FROM Sucursal s "
			+ "INNER JOIN s.terminal "
			+ "WHERE s.terminal.es_terminal = TRUE AND s.terminal.id_parada IN (SELECT l.destino.id_parada FROM Linea l WHERE l.habilitado = TRUE)"
		);
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
	
	public List<Parada> findOriginTerminalsByDestination(long id_parada) 
	{
		List<Parada> originTerminals = null;
		Query q = entityManager.createQuery(
			"SELECT DISTINCT l.origen FROM Linea l "
			+ "WHERE l.habilitado = true "
			+ "AND l.destino.id_parada = :idp "
			+ "AND l.origen.id_parada IN (SELECT s.terminal.id_parada FROM Sucursal s)"
		);
		q.setParameter("idp", id_parada);
		originTerminals = (List<Parada>)q.getResultList();
		return originTerminals;	
	}
	
	public List<Parada> findDestinationTerminalsByOrigin(long id_parada) 
	{
		List<Parada> destinationTerminals = null;
		Query q = entityManager.createQuery(
			"SELECT DISTINCT l.destino FROM Linea l "
			+ "WHERE l.habilitado = true "
			+ "AND l.origen.id_parada = :idp "
			+ "AND l.destino.id_parada IN (SELECT s.terminal.id_parada FROM Sucursal s)"
		);
		q.setParameter("idp", id_parada);
		destinationTerminals = (List<Parada>)q.getResultList();
		return destinationTerminals;	
	}

	public List<Parada> findStationsByDestinations(long id_parada) 
	{
		List<Parada> stations = new ArrayList<>();
		List<BigInteger> resultado = new ArrayList();
		//vuelve a la nativa porque linea_parada no esta mappeada
		Query q = entityManager.createNativeQuery("SELECT DISTINCT lp.paradas_id_parada "
											+"FROM Linea l "
											+"INNER JOIN linea_parada lp "
											+"ON lp.linea_id_linea = l.id_linea "
											+"INNER JOIN Parada p "
											+"on p.id_parada = lp.paradas_id_parada "
											+"WHERE l.habilitado = true "
											+"AND l.id_linea IN "
											+"( "
											+"SELECT lp.linea_id_linea "
											+"FROM linea_parada lp "
											+"WHERE lp.paradas_id_parada = :idp "
											+") "
											+"AND (lp.paradas_id_parada <  :idp OR l.id_parada_destino = :idp) "
											+"AND lp.paradas_id_parada <> l.id_parada_destino "
											+"AND l.id_parada_origen <> :idp ");		
	    q.setParameter("idp", id_parada);
		resultado = q.getResultList();
		for(int x = 0; x < resultado.size(); x++)
		{
			long auxid = resultado.get(x).longValue();
			Parada auxParada = new Parada();
			auxParada = findByID(auxid);
			stations.add(auxParada);
		}
		return stations;
	}
}
