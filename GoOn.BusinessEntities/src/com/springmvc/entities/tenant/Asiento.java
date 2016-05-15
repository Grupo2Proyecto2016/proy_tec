package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Asiento {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public long id_asiento;
	public long id_vehiculo;
	public int  numero;
	public Boolean es_ventana;
	public Boolean es_accesible;
	public Boolean habilitado;
	
	public long getId_asiento() {
		return id_asiento;
	}
	public void setId_asiento(long id_asiento) {
		this.id_asiento = id_asiento;
	}
	public long getId_vehiculo() {
		return id_vehiculo;
	}
	public void setId_vehiculo(long id_vehiculo) {
		this.id_vehiculo = id_vehiculo;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Boolean getEs_ventana() {
		return es_ventana;
	}
	public void setEs_ventana(Boolean es_ventana) {
		this.es_ventana = es_ventana;
	}
	public Boolean getEs_accesible() {
		return es_accesible;
	}
	public void setEs_accesible(Boolean es_accesible) {
		this.es_accesible = es_accesible;
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
}
