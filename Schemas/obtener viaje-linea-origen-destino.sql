SELECT 	v.id_viaje,
	CASE WHEN l.viaja_parado is true THEN ve.cantasientos + ve.cantparados
	ELSE ve.cantasientos
	END AS lugares,	
	v.inicio,
	l.numero,
	lpo.linea_id_linea,
	lpo.origen,
	po.descripcion origen_descripcion,
	lpd.destino,
	pd.descripcion destino_descripcion ,
	ve.id_vehiculo,
	ve.cantasientos
FROM
(
	SELECT lp.linea_id_linea, lp.paradas_id_parada AS origen FROM 
	linea_parada lp
	WHERE lp.paradas_id_parada IN (2)
) AS lpo
INNER JOIN
(
	SELECT lp.linea_id_linea, lp.paradas_id_parada AS destino FROM 
	linea_parada lp
	WHERE lp.paradas_id_parada IN (5)
) AS lpd
ON lpo.linea_id_linea = lpd.linea_id_linea

INNER JOIN linea l
ON l.id_linea = lpo.linea_id_linea

INNER JOIN parada po
ON po.id_parada = lpo.origen

INNER JOIN parada pd
ON pd.id_parada = lpd.destino

INNER JOIN viaje v
ON v.linea_id_linea = lpo.linea_id_linea

INNER JOIN vehiculo ve
ON ve.id_vehiculo = v.vehiculo_id_vehiculo


