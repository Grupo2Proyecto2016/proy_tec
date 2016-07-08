package com.springmvc.dataaccess.repository.tenant;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.main.EntityMapper;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Usuario;
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

	public List<Viaje> GetTravels(Date dateFrom) 
	{
		dateFrom.setHours(0);
		dateFrom.setMinutes(0);
		Calendar to = Calendar.getInstance();
		to.setTime(dateFrom);
		to.add(GregorianCalendar.DAY_OF_YEAR, 30);
		
		List<Viaje> travels = null;
		Query q = entityManager.createQuery("FROM Viaje "
				+ "WHERE terminado = FALSE "
				+ "AND inicio >= :from "
				+ "AND inicio <= :to "
				+ "ORDER BY inicio ASC");
		
		q.setParameter("from", dateFrom);
		q.setParameter("to", to.getTime());
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
		Query q = entityManager.createQuery("select max(id_pasaje) from Pasaje where viaje_id_viaje = :idv ");
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
				+ "AND v.terminado = FALSE "
				+ "AND v.linea.origen.id_parada = :origin "
				+ "AND v.linea.destino.id_parada = :destination "
				+ "AND v.inicio >= :tomorrow "
				+ "AND v.inicio <= :limit "
				//+ "GROUP BY v " 
				+ "AND v.vehiculo.cantEncomiendas > 0 AND size(v.encomiendas) < v.vehiculo.cantEncomiendas "
				+ "ORDER BY v.inicio ASC"
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
											+ "WHERE v.linea.id_linea in ("+auxin+")"
											+ "AND v.inicio > :dateFrom "
											+ "AND v.inicio < :dateTo "
											+ "AND v.terminado = FALSE "
											+ "WHERE v.linea.habilitado = TRUE "
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
														"pd.descripcion destino_descripcion, " +
														"ve.id_vehiculo, " +
														"ve.cantasientos " +
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
													"WHERE v.inicio > :dateFrom AND v.inicio < :dateTo "
													+ "AND v.terminado = FALSE ");		
		
		q.setParameter("dateFrom", dateFrom.getTime());
		dateFrom.add(Calendar.DAY_OF_YEAR, 1);
		q.setParameter("dateTo", dateFrom.getTime());
		
		viajes = EntityMapper.getResultList(q, ViajesBuscados.class);
		return viajes;
	}

	public List<Viaje> GetByBus(long idBus, Date from, Date to)
	{
		List<Viaje> travels = null;
		Query q = entityManager.createQuery(
				"FROM Viaje v "
				+ "WHERE v.linea.habilitado = TRUE "
				+ "AND v.vehiculo.id_vehiculo = :idBus "
				+ "AND v.inicio >= :from "
				+ "AND v.inicio <= :to "
				+ "AND v.terminado = FALSE "
		);
		q.setParameter("idBus", idBus); 
		q.setParameter("from", from);
		q.setParameter("to", to);
		
		travels = (List<Viaje>)q.getResultList();
		return travels;	
	}

	public List<Viaje> GetByDiverAndDate(long userId, Date from, Date to)
	{
		List<Viaje> travels = null;
		Query q = entityManager.createQuery(
				"FROM Viaje v "
				+ "WHERE v.linea.habilitado = TRUE "
				+ "AND v.conductor.id_usuario = :idu "
				+ "AND v.inicio >= :from "
				+ "AND v.inicio <= :to "
				+ "AND v.terminado = FALSE "
		);
		q.setParameter("idu", userId); 
		q.setParameter("from", from);
		q.setParameter("to", to);
		
		travels = (List<Viaje>)q.getResultList();
		return travels;	
	}

	public Viaje GetLastByDriver(Usuario user)
	{
		Calendar from = Calendar.getInstance();
		from.add(GregorianCalendar.MINUTE, -30);
		Calendar to = Calendar.getInstance();
		to.add(GregorianCalendar.MINUTE, 30);
		
		Viaje travel = null;
		Query q = entityManager.createQuery(
				"FROM Viaje v "
				+ "WHERE v.linea.habilitado = TRUE "
				+ "AND v.conductor.id_usuario = :idu "
				+ "AND v.inicio >= :from "
				+ "AND v.inicio <= :to "
				+ "AND v.terminado = FALSE "
				+ "ORDER BY v.inicio ASC"
		);
		q.setParameter("idu", user.getIdUsuario()); 
		q.setParameter("from", from.getTime());
		q.setParameter("to", to.getTime());
		q.setMaxResults(1);
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
}
