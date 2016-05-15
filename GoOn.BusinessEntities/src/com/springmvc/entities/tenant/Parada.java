package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Parada {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_parada;
	private String descripcion;
	private int id_pais;
	private int id_departamento;
	private int id_ciudad;
	private String coordenadas;
	private Boolean es_terminal;
	private Boolean es_peaje;
	private Boolean reajusta;
	
	public long getId_parada() {
		return id_parada;
	}
	public void setId_parada(long id_parada) {
		this.id_parada = id_parada;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getId_pais() {
		return id_pais;
	}
	public void setId_pais(int id_pais) {
		this.id_pais = id_pais;
	}
	public int getId_departamento() {
		return id_departamento;
	}
	public void setId_departamento(int id_departamento) {
		this.id_departamento = id_departamento;
	}
	public int getId_ciudad() {
		return id_ciudad;
	}
	public void setId_ciudad(int id_ciudad) {
		this.id_ciudad = id_ciudad;
	}
	public String getCoordenadas() {
		return coordenadas;
	}
	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}
	public Boolean getEs_terminal() {
		return es_terminal;
	}
	public void setEs_terminal(Boolean es_terminal) {
		this.es_terminal = es_terminal;
	}
	public Boolean getEs_peaje() {
		return es_peaje;
	}
	public void setEs_peaje(Boolean es_peaje) {
		this.es_peaje = es_peaje;
	}
	public Boolean getReajusta() {
		return reajusta;
	}
	public void setReajusta(Boolean reajusta) {
		this.reajusta = reajusta;
	}
}
