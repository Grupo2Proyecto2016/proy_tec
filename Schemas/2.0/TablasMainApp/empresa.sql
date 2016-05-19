-- Table: empresa

-- DROP TABLE empresa;

CREATE TABLE empresa
(
  id_empresa serial NOT NULL,
  nombre text NOT NULL,
  razon_social text NOT NULL,
  rut text NOT NULL,
  telefono text,
  direccion text,
  nombre_tenant text NOT NULL,
  id_pais bigint NOT NULL,
  CONSTRAINT empresa_pkey PRIMARY KEY (id_empresa),
  CONSTRAINT "empresa_pais_FK" FOREIGN KEY (id_pais)
      REFERENCES pais (id_pais) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE empresa
  OWNER TO postgres;
