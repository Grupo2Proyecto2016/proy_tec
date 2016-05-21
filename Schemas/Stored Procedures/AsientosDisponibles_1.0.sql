CREATE OR REPLACE FUNCTION asientosdisponibles(
    IN tamano_pagina integer DEFAULT 10,
    IN numeropagina integer DEFAULT 1,
    IN pdesde character varying DEFAULT NULL::character varying,
    IN phasta character varying DEFAULT NULL::character varying,
    IN pfecha date DEFAULT NULL::date)
  RETURNS TABLE(id_viaje bigint, id_vehiculo bigint) AS
$BODY$
DECLARE PageNumber BIGINT; 
BEGIN

 IF (tamano_pagina IS NOT NULL AND numeropagina IS NOT NULL) THEN
  PageNumber := (tamano_pagina * (numeropagina-1)); 
 END IF; 

 RETURN QUERY
 SELECT
  v.id_viaje, v.vehiculo_id_vehiculo 
 FROM public.Viaje v
 WHERE v.desde like '%'||pdesde||'%' and v.hasta like '%'||phasta||'%' and v.fecha = pfecha
 ORDER BY id_viaje desc
 LIMIT tamano_pagina
 OFFSET PageNumber; 
 
 EXCEPTION WHEN OTHERS THEN 
 RAISE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION asientosdisponibles(integer, integer, character varying, character varying, date)
  OWNER TO postgres;