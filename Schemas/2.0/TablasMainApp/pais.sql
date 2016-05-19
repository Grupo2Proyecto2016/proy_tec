-- Table: pais

-- DROP TABLE pais;

CREATE TABLE pais
(
  id_pais bigserial NOT NULL,
  nombre character varying(255),
  CONSTRAINT pais_pkey PRIMARY KEY (id_pais)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pais
  OWNER TO postgres;
