CREATE TABLE contacto
(
  id_contacto serial NOT NULL,
  id_empresa integer NOT NULL,
  nombre text NOT NULL,
  apellido text,
  mail text,
  descripcion text,
  CONSTRAINT contacto_pkey PRIMARY KEY (id_contacto),
  CONSTRAINT contactoempresafk FOREIGN KEY (id_empresa)
      REFERENCES empresa (id_empresa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE contacto
  OWNER TO postgres;

CREATE INDEX fki_contactoempresafk
  ON contacto
  USING btree
  (id_empresa);
