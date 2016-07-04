package com.springmvc.dataaccess.repository.tenant;

import java.math.BigInteger;
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
		Query q = entityManager.createNativeQuery("SELECT id_pasaje  FROM " +
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
												"		WHERE l.id_linea = " + id_linea +
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

}
