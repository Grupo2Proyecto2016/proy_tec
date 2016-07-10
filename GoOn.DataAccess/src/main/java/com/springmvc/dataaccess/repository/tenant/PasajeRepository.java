package com.springmvc.dataaccess.repository.tenant;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.springmvc.entities.tenant.Devolucion;
import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.enums.TicketStatus;
import com.springmvc.exceptions.CollectTicketException;

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

	public int getCantVendidos(long origen, long destino, long linea_id_linea, long id_viaje) 
	{
		Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM " +
											"( " +
											"	SELECT vp.id_pasaje, array_agg(lp.id_parada) AS paradas FROM " +
											"	( " +
											"		SELECT " + 
											"		pas.id_pasaje, " +
											"		CASE WHEN pas.parada_sube_id_parada = l.id_parada_origen THEN -1 " +
											"		ELSE pas.parada_sube_id_parada " +
											"		END AS parada_sube, " +
											" " +
											"		CASE WHEN pas.parada_baja_id_parada = l.id_parada_destino THEN 1000 " +
											"		ELSE pas.parada_baja_id_parada " +
											"		END AS parada_baja " +
											" " +
											"		FROM linea l " +
											"		INNER JOIN viaje v " +
											"			ON v.linea_id_linea = l.id_linea " +
											"		INNER JOIN pasaje pas " +
											"			ON pas.viaje_id_viaje = v.id_viaje " +
											"		WHERE v.id_viaje = " + id_viaje +
											"		AND l.habilitado = true " +
											"	) AS vp " +
											" " +
											"	INNER JOIN " +
											" " +
											"	( " +
											"		SELECT  CASE WHEN p.id_parada = l.id_parada_destino THEN 1000 " +
											"			WHEN p.id_parada = l.id_parada_origen THEN -1 " +
											"			ELSE p.id_parada " +
											"		       END AS id_parada " +
											"		FROM linea_parada lp " +
											"		INNER JOIN linea l " +
											"			ON l.id_linea = lp.linea_id_linea " +
											"		INNER JOIN parada p " +
											"			ON lp.paradas_id_parada = p.id_parada " +
											"		WHERE l.id_linea = " + linea_id_linea +
											"	) AS lp " +
											" " +
											"	ON vp.parada_sube <= lp.id_parada AND vp.parada_baja >= lp.id_parada " +
											"	GROUP BY vp.id_pasaje " +
											") AS x " +
											" " +
											"INNER JOIN " + 
											" " +
											"( " +
											"	SELECT array_agg(kk.id_parada) tf FROM " +
											"	( " +
											"		SELECT  CASE WHEN lp.paradas_id_parada = l.id_parada_destino THEN 1000 " +
											"			WHEN lp.paradas_id_parada = l.id_parada_origen THEN -1 " +
											"			ELSE lp.paradas_id_parada " +
											"			END AS id_parada, " +
											" " +
											"			CASE WHEN "+ origen +" = l.id_parada_destino THEN 1000 " +
											"			WHEN "+ origen +" = l.id_parada_origen THEN -1 " +
											"			ELSE "+ origen +
											"			END AS origen , " +
											" " +
											"			CASE WHEN "+ destino +" = l.id_parada_destino THEN 1000 " +
											"			WHEN "+ destino +" = l.id_parada_origen THEN -1 " +
											"			ELSE "+ destino + 
											"			END AS destino " +
											"		FROM linea_parada lp " +
											"		INNER JOIN linea l " +
											"			ON l.id_linea = lp.linea_id_linea " +
											"		WHERE lp.linea_id_linea = " + linea_id_linea +
											"	) AS kk " +
											"	WHERE kk.id_parada > kk.origen AND kk.id_parada <= kk.destino " +  
											"	GROUP BY kk.origen, kk.destino " +
											") AS z " +
											" " +
											"ON x.paradas && z.tf");

		BigInteger cantidad = (BigInteger) q.getSingleResult();
		return cantidad.intValue();
	}

	public List<Long> getListaReservados(int origen, int destino, int id_linea, int id_viaje) 
	{
		List<BigInteger> resultado = new ArrayList();
		List<Long> reservados = new ArrayList();
		Query q = entityManager.createNativeQuery("SELECT x.asiento  FROM " +
													"( " +
													"	SELECT vp.id_pasaje, vp.asiento, array_agg(lp.id_parada) AS paradas FROM " +
													"	( " +
													"		SELECT " + 
													"		pas.id_pasaje, " +
													"		pas.asiento_id_asiento AS asiento, " +
													"		CASE WHEN pas.parada_sube_id_parada = l.id_parada_origen THEN -1 " +
													"		ELSE pas.parada_sube_id_parada " +
													"		END AS parada_sube, " +
													" " +
													"		CASE WHEN pas.parada_baja_id_parada = l.id_parada_destino THEN 1000 " +
													"		ELSE pas.parada_baja_id_parada " +
													"		END AS parada_baja " +
													" " +
													"		FROM linea l " +
													"		INNER JOIN viaje v " +
													"			ON v.linea_id_linea = l.id_linea " +
													"		INNER JOIN pasaje pas " +
													"			ON pas.viaje_id_viaje = v.id_viaje " +
													"		WHERE v.id_viaje = " + id_viaje +
													"		AND l.habilitado = true " +
													"	) AS vp " +
													" " +
													"	INNER JOIN " +
													" " +
													"	( " +
													"		SELECT  CASE WHEN p.id_parada = l.id_parada_destino THEN 1000 " +
													"			WHEN p.id_parada = l.id_parada_origen THEN -1 " +
													"			ELSE p.id_parada " +
													"		       END AS id_parada " +
													"		FROM linea_parada lp " +
													"		INNER JOIN linea l " +
													"			ON l.id_linea = lp.linea_id_linea " +
													"		INNER JOIN parada p " +
													"			ON lp.paradas_id_parada = p.id_parada " +
													"		WHERE l.id_linea = " + id_linea +
													"	) AS lp " +
													" " +
													"	ON vp.parada_sube <= lp.id_parada AND vp.parada_baja >= lp.id_parada " +
													"	GROUP BY vp.id_pasaje, vp.asiento " +
													") AS x " +
													" " +
													"INNER JOIN " + 
													" " +
													"( " +
													"	SELECT array_agg(kk.id_parada) tf FROM " +
													"	( " +
													"		SELECT  CASE WHEN lp.paradas_id_parada = l.id_parada_destino THEN 1000 " +
													"			WHEN lp.paradas_id_parada = l.id_parada_origen THEN -1 " +
													"			ELSE lp.paradas_id_parada " +
													"			END AS id_parada, " +
													" " +
													"			CASE WHEN "+ origen +" = l.id_parada_destino THEN 1000 " +
													"			WHEN "+ origen +" = l.id_parada_origen THEN -1 " +
													"			ELSE "+ origen +
													"			END AS origen, " +
													" " +
													"			CASE WHEN "+ destino +" = l.id_parada_destino THEN 1000 " +
													"			WHEN "+ destino +" = l.id_parada_origen THEN -1 " +
													"			ELSE "+ destino +
													"			END AS destino " +
													"		FROM linea_parada lp " +
													"		INNER JOIN linea l " +
													"			ON l.id_linea = lp.linea_id_linea " +
													"		WHERE lp.linea_id_linea = " + id_linea +
													"	) AS kk " +
													"	WHERE kk.id_parada > kk.origen AND kk.id_parada <= kk.destino " +  
													"	GROUP BY kk.origen, kk.destino " +
													") AS z " +
													" " +
													"ON x.paradas && z.tf"); 
		resultado = q.getResultList();
		for(int x = 0; x < resultado.size(); x++)
		{
			reservados.add(resultado.get(x).longValue());
		}
		return reservados;
	}

	public Double getValorPasaje(long origen, long destino, long id_linea)
	{
		Query q = entityManager.createNativeQuery("SELECT " + 
													"	CASE WHEN r.costo_minimo IS NULL OR r.all_travel THEN r.costo_maximo " +
													"	ELSE SUM(r.reajuste) + r.costo_minimo " +
													"	END AS costo " +
													"FROM " +
													"( " +
													"	SELECT " + 
													"		CASE WHEN p.id_parada = "+ origen +" THEN 0 " +
													"		ELSE p.reajuste " +
													"		END AS reajuste, " +
													" " +		
													"		l.costo_minimo, " +
													"		l.costo_maximo, " +
													" " +		
													"		CASE WHEN lp.paradas_id_parada = l.id_parada_destino THEN 1000 " +
													"		WHEN lp.paradas_id_parada = l.id_parada_origen THEN -1 " +
													"		ELSE lp.paradas_id_parada " +
													"		END AS id_parada, " +
													" " +
													"		CASE WHEN "+ origen +" = l.id_parada_destino THEN 1000 " +
													"		WHEN "+ origen +" = l.id_parada_origen THEN -1 " +
													"		ELSE "+ origen +
													"		END AS origen, " +
													" " +
													"		CASE WHEN "+ destino +" = l.id_parada_destino THEN 1000 " +
													"		WHEN "+ destino +" = l.id_parada_origen THEN -1 " +
													"		ELSE "+ destino +
													"		END AS destino, " +
													" " +
													"		CASE WHEN " + 
													"			("+ destino +" = l.id_parada_destino OR "+ destino +" = l.id_parada_origen) " +
													"			AND  ("+ origen +" = l.id_parada_destino OR "+ origen +" = l.id_parada_origen) THEN TRUE " +
													"		ELSE FALSE " +
													"		END AS all_travel " +
													" " +		
													"	FROM linea_parada lp " +
													"	INNER JOIN linea l " +
													"		ON l.id_linea = lp.linea_id_linea " +
													"	INNER JOIN parada p " +
													"		ON p.id_parada = lp.paradas_id_parada " +
													"	WHERE lp.linea_id_linea = " + id_linea +
													" ) AS r " +
													" WHERE r.id_parada >= r.origen AND r.id_parada <= r.destino " +  
													" GROUP BY r.costo_minimo, r.all_travel, r.costo_maximo");
		return (Double) q.getSingleResult();		
	}

	public List<Pasaje> GetByStatusAndTime(TicketStatus status, Date date)
	{
		List<Pasaje> result = new ArrayList<>();
		Query q = entityManager.createQuery("SELECT p FROM Pasaje p "
				+ "WHERE p.estado = :status "
				+ "AND p.viaje.inicio <= :date "
		);
		q.setParameter("status", status.getValue());
		q.setParameter("date", date);
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

	public void deleteTicket(Pasaje ticket) 
	{
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			entityManager.remove(ticket);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public List<Pasaje> GetByTravel(long travelId)
	{
		List<Pasaje> result = new ArrayList<>();
		Query q = entityManager.createQuery("SELECT p FROM Pasaje p WHERE p.viaje.id_viaje = :idv "
		);
		q.setParameter("idv", travelId);
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
	
	public void updateByTravel(long travelId, TicketStatus status) 
	{
		List<Pasaje> travels = GetByTravel(travelId);
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			for (Pasaje pasaje : travels) 
			{
				pasaje.setEstado(status.getValue());
			}
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public Pasaje GetByNumber(String ticketNumber) 
	{
		Pasaje result = null;
		Query q = entityManager.createQuery("SELECT p FROM Pasaje p WHERE p.numero = :num ");
		q.setParameter("num", ticketNumber);
		try
		{
			result = (Pasaje)q.getSingleResult();
		}
		catch(NoResultException ex)
		{
			return null;
		}
		return result;
	}

	public void Collect(Pasaje ticket) 
	{
		EntityTransaction t = entityManager.getTransaction();
		
		try
		{
			t.begin();
			ticket.setEstado(TicketStatus.cashed.getValue());
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}

	public List<Pasaje> GetActive(Date from, Date to) 
	{
		List<Pasaje> result = new ArrayList<>();
		Query q = entityManager.createQuery("SELECT p FROM Pasaje p "
				+ "WHERE p.viaje.inicio >= :from AND p.viaje.inicio <= :to "
				+ "AND (p.estado = 1 OR p.estado = 2) "
				+ "ORDER BY p.viaje.inicio ASC "
		);
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

	public void AddRefund(Devolucion refund) 
	{
		EntityTransaction t = entityManager.getTransaction();
		try
		{
			t.begin();	
			entityManager.persist(refund);
			entityManager.flush();
			t.commit();
		}
		catch(Exception ex)
		{
			t.rollback();
			throw ex;
		}
	}
	
}
