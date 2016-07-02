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
import com.springmvc.entities.tenant.ViajesBuscados;

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
		//como se busca por fecha, entra en juego la comparacion por hora, en este caso es 00:00:00
		//entonces tiene que ser entre 2 dias para que entre en la clausula where
		q.setParameter("dateFrom", dateFrom.getTime());
		dateFrom.add(Calendar.DAY_OF_YEAR, 1);
		q.setParameter("dateTo", dateFrom.getTime());
		travels = (List<Viaje>)q.getResultList();
		return travels;
	}

	public List<ViajesBuscados> getTravelsAdvanced(List<Integer> origins, List<Integer> destinations,Calendar dateFrom) 
	{
		List<ViajesBuscados> viajes = null;
		
		String lstOrigins = ""; //cambiar por funcion
		for (int i = 0; i < origins.size(); i++) 
		{
			if (i == 0)
			{
				lstOrigins = lstOrigins + origins.get(i);				
			}
			else
			{
				lstOrigins = lstOrigins + "," + origins.get(i);
			}
		}
		
		String lstDestinatios = "";
		for (int i = 0; i < destinations.size(); i++) 
		{
			if (i == 0)
			{
				lstDestinatios = lstDestinatios + destinations.get(i);				
			}
			else
			{
				lstDestinatios = lstDestinatios + "," + destinations.get(i);
			}
		}
		
		Query q = entityManager.createNativeQuery("SELECT 	v.id_viaje, " +
														"CASE WHEN l.viaja_parado is true THEN ve.cantasientos + ve.cantparados " +
														"ELSE ve.cantasientos " +
														"END AS lugares, " +
														"v.inicio, " +
														"l.numero, " +
														"lpo.linea_id_linea, " +
														"lpo.origen, " +
														"po.descripcion origen_descripcion, " +
														"lpd.destino, " +
														"pd.descripcion destino_descripcion " + 
													"FROM " +
													"( " +
														"SELECT lp.linea_id_linea, lp.paradas_id_parada AS origen FROM " + 
														"linea_parada lp " +
														"WHERE lp.paradas_id_parada IN (" + lstOrigins + ") " +
													") AS lpo " +
													"INNER JOIN " +
													"( " +
														"SELECT lp.linea_id_linea, lp.paradas_id_parada AS destino FROM " + 
														"linea_parada lp " +
														"WHERE lp.paradas_id_parada IN (" + lstDestinatios + ") " +
													") AS lpd " +
													"ON lpo.linea_id_linea = lpd.linea_id_linea " +
													" " +
													"INNER JOIN linea l " +
													"ON l.id_linea = lpo.linea_id_linea " +
													" " +
													"INNER JOIN parada po " +
													"ON po.id_parada = lpo.origen " +
													" " +
													"INNER JOIN parada pd " +
													"ON pd.id_parada = lpd.destino " +
													" " +
													"INNER JOIN viaje v " +
													"ON v.linea_id_linea = lpo.linea_id_linea " +
													" " + 													
													"INNER JOIN vehiculo ve " +
													"ON ve.id_vehiculo = v.vehiculo_id_vehiculo " +
													"WHERE v.inicio > :dateFrom AND v.inicio < :dateTo");		
		
		q.setParameter("dateFrom", dateFrom.getTime());
		dateFrom.add(Calendar.DAY_OF_YEAR, 1);
		q.setParameter("dateTo", dateFrom.getTime());
		
		viajes = (List<ViajesBuscados>)q.getResultList();
		
		return viajes;
	}
}
