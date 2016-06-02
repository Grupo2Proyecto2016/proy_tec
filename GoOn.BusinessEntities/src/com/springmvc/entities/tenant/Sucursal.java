package com.springmvc.entities.tenant;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Sucursal {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_sucursal;
	private String nombre;
	private String direccion;
	private String telefono;
	private String mail;
	private double latitud;
	private double longitud;
	
	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn (name="id_parada")
	@JsonBackReference
	private Parada terminal;
	
	public Parada getTerminal() {
		return terminal;
	}
	public void setTerminal(Parada terminal) {
		this.terminal = terminal;
	}
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
	
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
}
