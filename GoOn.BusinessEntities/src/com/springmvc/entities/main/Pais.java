package com.springmvc.entities.main;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pais {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_pais;
	private String nombre;

	public long getId_pais() {
		return id_pais;
	}
	public void setId_pais(long id_pais) {
		this.id_pais = id_pais;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}