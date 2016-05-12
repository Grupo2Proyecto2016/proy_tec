CREATE TABLE empresa_modulo
(
  id_empresa integer NOT NULL,
  id_modulo integer NOT NULL,
  CONSTRAINT moduloempresafk FOREIGN KEY (id_empresa)
      REFERENCES empresa (id_empresa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT modulomodulofk FOREIGN KEY (id_modulo)
      REFERENCES modulo (id_modulo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE empresa_modulo
  OWNER TO postgres;

CREATE INDEX fki_moduloempresafk
  ON empresa_modulo
  USING btree
  (id_empresa);


CREATE INDEX fki_modulomodulofk
  ON empresa_modulo
  USING btree
  (id_modulo);
