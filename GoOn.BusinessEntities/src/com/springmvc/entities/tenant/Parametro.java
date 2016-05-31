package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Parametro {
	
	@Id
	private int id;
	private String nombre;
	private float valor;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public float getValor() {
		return valor;
	}
	
	public void setValor(float valor) {
		this.valor = valor;
	}

}
