-- Table: usuario

-- DROP TABLE usuario;

CREATE TABLE usuario
(
  id_usuario serial NOT NULL,
  usrname text NOT NULL,
  passwd text NOT NULL,
  nombre text NOT NULL,
  apellido text NOT NULL,
  email text NOT NULL,
  puede_crear boolean,
  ultimo_reset_password date,
  enabled boolean NOT NULL,
  CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuario
  OWNER TO postgres;
