package com.springmvc.dataaccess.repository.tenant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.entities.tenant.Viaje;

public class ViajeRepository {

	EntityManager entityManager;
	
	public ViajeRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	public Viaje FindByID(long travelId)
	{
		Viaje travel = null;
		Query q = entityManager.createQuery("FROM Viaje WHERE id_viaje = :idv");
		q.setParameter("idv", travelId);
		try
		{
			travel = (Viaje)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return travel;
	}

	public void InsertTravel(Viaje travel) 
	{
		travel.setEncomiendas(new ArrayList<>());
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(travel);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public List<Viaje> GetTravels() 
	{
		List<Viaje> travels = null;
		Query q = entityManager.createQuery("FROM Viaje");
		travels = (List<Viaje>)q.getResultList();
		return travels;	
	}

	public void DeleteTravel(long travelId) 
	{
		EntityTransaction t = entityManager.getTransaction();
		Viaje travel = FindByID(travelId);				
		try
		{
			t.begin();
			entityManager.remove(travel);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public boolean HasPackages(long travelId) 
	{
		Query q = entityManager.createQuery("select max(id_encomienda) from Encomienda where id_viaje = :idv");
		q.setParameter("idv", travelId);
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
		return (maxID > 0);
	}

	public boolean HasTickets(long travelId) 
	{
		Query q = entityManager.createQuery("select max(id_pasaje) from Pasaje where viaje_id_viaje = :idv");
		q.setParameter("idv", travelId);
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
		return (maxID > 0);
	}

	public List<Viaje> GetPackageTravels(long origin, long destination, Calendar tomorrow, Calendar limit) 
	{
		List<Viaje> travels = null;
		Query q = entityManager.createQuery(
				"FROM Viaje v "
				//+"LEFT JOIN v.encomiendas e " 
				+ "WHERE v.linea.habilitado = TRUE "
				+ "AND v.linea.origen.id_parada = :origin "
				+ "AND v.linea.destino.id_parada = :destination "
				+ "AND v.inicio > :tomorrow "
				+ "AND v.inicio < :limit "
				//+ "GROUP BY v " 
				+ "AND v.vehiculo.cantEncomiendas > 0 AND size(v.encomiendas) < v.vehiculo.cantEncomiendas"
		);
		q.setParameter("origin", origin);
		q.setParameter("destination", destination);
		q.setParameter("tomorrow", tomorrow.getTime());
		q.setParameter("limit", limit.getTime());
		travels = (List<Viaje>)q.getResultList();
		return travels;	
	}

	public List<Viaje> GetLineTravels(List<Long> id_lineas, Calendar dateFrom) 
	{
		List<Viaje> travels = null;
		String auxin = "";
		for (int i = 0; i < id_lineas.size(); i++) 
		{
			if (i == 0)
			{
				auxin = auxin + id_lineas.get(i);				
			}
			else
			{
				auxin = auxin + "," + id_lineas.get(i);
			}
		}
		Query q = entityManager.createQuery("FROM Viaje v "
											//+ "WHERE v.inicio = :dateFrom "											
											+ "WHERE v.linea.id_linea in ("+auxin+")"
											+ "AND v.inicio > :dateFrom "
											+ "AND v.inicio < :dateTo "
											/*+ "WHERE v.linea.habilitado = TRUE "
											+ "AND v.inicio = :dateFrom "	
											+ "AND linea_id_linea in ("+auxin+")"*/
		);
		q.setParameter("dateFrom", dateFrom.getTime());
		dateFrom.add(Calendar.DAY_OF_YEAR, 1);
		q.setParameter("dateTo", dateFrom.getTime());
		travels = (List<Viaje>)q.getResultList();
		return travels;
	}
}
