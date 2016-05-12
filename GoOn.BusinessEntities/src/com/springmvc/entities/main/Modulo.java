package com.springmvc.entities.main;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Modulo {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id_modulo;	
	private String nombre;
	private String descripcion;
	
	public long getId_modulo() {
		return id_modulo;
	}
	public void setId_modulo(long id_modulo) {
		this.id_modulo = id_modulo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
