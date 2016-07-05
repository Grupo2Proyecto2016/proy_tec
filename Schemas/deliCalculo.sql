SELECT 
	CASE WHEN r.costo_minimo IS NULL OR r.all_travel THEN r.costo_maximo
	ELSE SUM(r.reajuste) + r.costo_minimo
	END AS costo
FROM
(
	SELECT 
		CASE WHEN p.id_parada = 1 THEN 0
		ELSE p.reajuste
		END AS reajuste,
		
		l.costo_minimo,
		l.costo_maximo,
		
		CASE WHEN lp.paradas_id_parada = l.id_parada_destino THEN 1000
		WHEN lp.paradas_id_parada = l.id_parada_origen THEN -1
		ELSE lp.paradas_id_parada
		END AS id_parada,

		CASE WHEN 10 = l.id_parada_destino THEN 1000
		WHEN 10 = l.id_parada_origen THEN -1
		ELSE 10
		END AS origen,

		CASE WHEN 13 = l.id_parada_destino THEN 1000
		WHEN 13 = l.id_parada_origen THEN -1
		ELSE 13
		END AS destino,

		CASE WHEN 
			(13 = l.id_parada_destino OR 13 = l.id_parada_origen)
			AND  (10 = l.id_parada_destino OR 10 = l.id_parada_origen) THEN TRUE
		ELSE FALSE
		END AS all_travel
		
	FROM linea_parada lp
	INNER JOIN linea l
		ON l.id_linea = lp.linea_id_linea
	INNER JOIN parada p
		ON p.id_parada = lp.paradas_id_parada
	WHERE lp.linea_id_linea = 5
) AS r
WHERE r.id_parada >= r.origen AND r.id_parada <= r.destino  
GROUP BY r.costo_minimo, r.all_travel, r.costo_maximo