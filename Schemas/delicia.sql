SELECT COUNT(*)  FROM
(
	SELECT vp.id_pasaje, array_agg(lp.id_parada) AS paradas FROM
	(
		SELECT 
		pas.id_pasaje,
		CASE WHEN pas.parada_sube_id_parada = l.id_parada_origen THEN -1
		ELSE pas.parada_sube_id_parada
		END AS parada_sube,

		CASE WHEN pas.parada_baja_id_parada = l.id_parada_destino THEN 1000
		ELSE pas.parada_baja_id_parada
		END AS parada_baja

		FROM linea l
		INNER JOIN viaje v
			ON v.linea_id_linea = l.id_linea
		INNER JOIN pasaje pas
			ON pas.viaje_id_viaje = v.id_viaje
		WHERE v.id_viaje = 1
		AND l.habilitado = true
	) AS vp

	INNER JOIN

	(
		SELECT  CASE WHEN p.id_parada = l.id_parada_destino THEN 1000
			WHEN p.id_parada = l.id_parada_origen THEN -1
			ELSE p.id_parada
		       END AS id_parada
		FROM linea_parada lp
		INNER JOIN linea l
			ON l.id_linea = lp.linea_id_linea
		INNER JOIN parada p
			ON lp.paradas_id_parada = p.id_parada
		WHERE l.id_linea = 1
	) AS lp

	ON vp.parada_sube <= lp.id_parada AND vp.parada_baja >= lp.id_parada
	GROUP BY vp.id_pasaje
) AS x

INNER JOIN 

(
	SELECT array_agg(kk.id_parada) tf FROM
	(
		SELECT  CASE WHEN lp.paradas_id_parada = l.id_parada_destino THEN 1000
			WHEN lp.paradas_id_parada = l.id_parada_origen THEN -1
			ELSE lp.paradas_id_parada
			END AS id_parada,

			CASE WHEN 3 = l.id_parada_destino THEN 1000
			WHEN 3 = l.id_parada_origen THEN -1
			ELSE 3
			END AS origen,

			CASE WHEN 2 = l.id_parada_destino THEN 1000
			WHEN 2 = l.id_parada_origen THEN -1
			ELSE 2
			END AS destino
		FROM linea_parada lp
		INNER JOIN linea l
			ON l.id_linea = lp.linea_id_linea
		WHERE lp.linea_id_linea = 1
	) AS kk
	WHERE kk.id_parada > kk.origen AND kk.id_parada <= kk.destino  
	GROUP BY kk.origen, kk.destino
) AS z

ON x.paradas && z.tf