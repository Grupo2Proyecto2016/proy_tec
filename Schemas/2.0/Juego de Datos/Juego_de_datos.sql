/*  RESETEO DE SECUENCIAS  */

ALTER SEQUENCE asiento_id_asiento_seq RESTART WITH 1;
ALTER SEQUENCE ciudad_id_ciudad_seq RESTART WITH 1;
ALTER SEQUENCE configuracionglobal_id_configuracion_seq RESTART WITH 1;
ALTER SEQUENCE departamento_id_departamento_seq RESTART WITH 1;	
ALTER SEQUENCE encomienda_id_encomienda_seq RESTART WITH 1;
ALTER SEQUENCE linea_id_linea_seq RESTART WITH 1;
ALTER SEQUENCE mantenimiento_id_mantenimiento_seq RESTART WITH 1;
ALTER SEQUENCE pago_id_pago_seq RESTART WITH 1;
ALTER SEQUENCE pais_id_pais_seq RESTART WITH 1;
ALTER SEQUENCE parada_id_parada_seq RESTART WITH 1;
ALTER SEQUENCE pasaje_id_pasaje_seq RESTART WITH 1;
ALTER SEQUENCE rol_id_rol_seq RESTART WITH 1;
ALTER SEQUENCE taller_id_taller_seq RESTART WITH 1;
ALTER SEQUENCE usuario_id_usuario_seq RESTART WITH 1;
ALTER SEQUENCE vehiculo_id_vehiculo_seq RESTART WITH 1;
ALTER SEQUENCE viaje_id_viaje_seq RESTART WITH 1;

/*  ROLES */
insert into rol(descripcion, nombre) values ('ADMIN', 'Administrador del sitio.');
insert into rol(descripcion, nombre) values ('SALE', 'Empleado de Mostrador, pasajes y encomiendas.');
insert into rol(descripcion, nombre) values ('DRIVER', 'Conductor del vehiculo.');
insert into rol(descripcion, nombre) values ('CLIENT', 'Usuario registrado en el sitio.');