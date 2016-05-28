package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sucursal {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_sucursal;
	private String nombre;
	private String direccion;
	private String telefono;
	private String mail;
	private long latitud;
	private long longitud;
	public long getId_sucursal() {
		return id_sucursal;
	}
	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public long getLatidud() {
		return latitud;
	}
	public void setLatidud(long latidud) {
		this.latitud = latidud;
	}
	public long getLongitud() {
		return longitud;
	}
	public void setLongitud(long longitud) {
		this.longitud = longitud;
	}
}
