package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Taller {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_taller;
	private String nombre;
	private long id_departamento;
	private long id_ciudad;
	private String Direccion;
	private String Telefono;
	
	public long getId_taller() {
		return id_taller;
	}
	public void setId_taller(long id_taller) {
		this.id_taller = id_taller;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public long getId_departamento() {
		return id_departamento;
	}
	public void setId_departamento(long id_departamento) {
		this.id_departamento = id_departamento;
	}
	public long getId_ciudad() {
		return id_ciudad;
	}
	public void setId_ciudad(long id_ciudad) {
		this.id_ciudad = id_ciudad;
	}
	public String getDireccion() {
		return Direccion;
	}
	public void setDireccion(String direccion) {
		Direccion = direccion;
	}
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
}
