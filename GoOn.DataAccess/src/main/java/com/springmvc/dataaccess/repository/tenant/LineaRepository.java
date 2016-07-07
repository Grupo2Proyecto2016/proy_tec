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
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.entities.tenant.Viaje;

public class LineaRepository {
	
	EntityManager entityManager;
	
	public LineaRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public void InsertLine(Linea linea)
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(linea);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public Linea findByID(long id_linea)
	{
		Linea linea = null;
		Query q = entityManager.createQuery("FROM Linea WHERE id_linea = :idl");
		q.setParameter("idl", id_linea);
		try
		{
			linea = (Linea)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return linea;
	}
	
	public List<Linea> getLineas() 
	{
		List<Linea> lineas = null;
		Query q = entityManager.createQuery("FROM Linea where habilitado = true");
		lineas = (List<Linea>)q.getResultList();
		return lineas;		
	}

	public boolean TieneViajes(long id_linea) 
	{	
		Query q = entityManager.createQuery("select max(id_viaje) from Viaje where linea_id_linea = :idl");
		q.setParameter("idl", id_linea);
		long maxID;
		try
		{
			maxID = (long) q.getSingleResult();
		} 
		catch(NoResultException ex) 
		{
			return false;
		}
		catch(NullPointerException ex) 
		{
			return false;
		}
		return (maxID>0);
	}

	public void deleteLinea(long id_linea) 
	{	
		EntityTransaction t = entityManager.getTransaction();
		Linea linea = findByID(id_linea);				
		try
		{
			t.begin();
			linea.setHabilitado(false);
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
	public List<Linea> getLineasbyIdOrigins(List<Integer> origins) 
	{
		return getLineasbyIdDestinations(origins);
	}

	public List<Linea> getLineasbyIdDestinations(List<Integer> destinations) 
	{
		List<Linea> lineas = new ArrayList<Linea>();
		List<BigInteger> resultados = new ArrayList();
		String auxin = "";
		for (int i = 0; i < destinations.size(); i++) 
		{
			if (i == 0)
			{
				auxin = auxin + destinations.get(i);				
			}
			else
			{
				auxin = auxin + "," + destinations.get(i);
			}
		}
		Query q = entityManager.createNativeQuery("SELECT DISTINCT l.id_linea FROM Linea l,  Parada p, linea_parada lp "
											+ "where l.habilitado = true "
											+ "and lp.linea_id_linea = l.id_linea "
											+ "and lp.paradas_id_parada in ("+auxin+")");				
		resultados = q.getResultList();
		for (int i = 0; i < resultados.size(); i++) 
		{			
			Linea axuLinea = findByID(resultados.get(i).longValue());
			lineas.add(axuLinea);
		}
		return lineas;				
	}

	public boolean lineExists(long linenumber) 
	{
		Query q = entityManager.createQuery("select count(*) from Linea where id_linea = :idl");
		q.setParameter("idl", linenumber);
		long cantidad;
		try
		{
			cantidad = (long) q.getSingleResult();
		} 
		catch(NoResultException ex) 
		{
			return false;
		}
		catch(NullPointerException ex) 
		{
			return false;
		}
		
		return (cantidad >= 2);	
	}

	public List<Long> getidLineasbyIdOrigins(List<Integer> origins) 
	{
		List<BigInteger> resultados = new ArrayList();
		List<Long> lista_longs = new ArrayList(); 
		String auxin = "";
		for (int i = 0; i < origins.size(); i++) 
		{
			if (i == 0)
			{
				auxin = auxin + origins.get(i);				
			}
			else
			{
				auxin = auxin + "," + origins.get(i);
			}
		}
		Query q = entityManager.createNativeQuery("SELECT DISTINCT l.id_linea FROM Linea l,  Parada p, linea_parada lp "
											+ "where l.habilitado = true "
											+ "and lp.linea_id_linea = l.id_linea "
											+ "and lp.paradas_id_parada in ("+auxin+")");
		
		resultados = q.getResultList();		
		for (int i = 0; i < resultados.size(); i++) 
		{
			lista_longs.add(resultados.get(i).longValue());
		}
		return lista_longs;		
	}

	public void InsertTicket(Pasaje ticketToPersist) 
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(ticketToPersist);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}			
	}

//	public List<Parada> findDestinationTerminalsByOrigin(long id_parada) 
//	{
//		List<Parada> originTerminals = null;
//		Query q = entityManager.createQuery("SELECT DISTINCT l.destino FROM Linea l where l.habilitado = true AND l.origen.id_parada = :idp");
//		q.setParameter("idp", id_parada);
//		originTerminals = (List<Parada>)q.getResultList();
//		return originTerminals;	
//	}
}
