CREATE TABLE usuario
(
  id_usuario serial NOT NULL,
  usrname text NOT NULL,
  passwd text NOT NULL,
  nombre text NOT NULL,
  apellido text NOT NULL,
  email text NOT NULL,
  puede_crear boolean,
  CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuario
  OWNER TO postgres;
