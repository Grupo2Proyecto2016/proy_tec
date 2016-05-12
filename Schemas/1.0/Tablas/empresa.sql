CREATE TABLE empresa
(
  id_empresa serial NOT NULL,
  nombre text NOT NULL,
  razon_social text NOT NULL,
  rut text NOT NULL,
  telefono text,
  pais text,
  direccion text,
  nombre_tenant text NOT NULL,
  CONSTRAINT empresa_pkey PRIMARY KEY (id_empresa)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE empresa
  OWNER TO postgres;