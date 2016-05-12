CREATE TABLE modulo
(
  id_modulo serial NOT NULL,
  nombre text NOT NULL,
  descripcion text NOT NULL,
  CONSTRAINT modulo_pkey PRIMARY KEY (id_modulo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE modulo
  OWNER TO postgres;
