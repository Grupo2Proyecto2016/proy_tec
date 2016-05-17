package com.springmvc.entities.main;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ciudad {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_ciudad;
	private long id_departamento;
	private long id_pais;
	private String nombre;
	public long getId_ciudad() {
		return id_ciudad;
	}
	public void setId_ciudad(long id_ciudad) {
		this.id_ciudad = id_ciudad;
	}
	public long getId_departamento() {
		return id_departamento;
	}
	public void setId_departamento(long id_departamento) {
		this.id_departamento = id_departamento;
	}
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
