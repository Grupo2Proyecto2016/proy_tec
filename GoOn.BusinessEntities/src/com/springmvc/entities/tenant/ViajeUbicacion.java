package com.springmvc.entities.tenant;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class ViajeUbicacion
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch=FetchType.EAGER)
	private Viaje viaje; 
	private Date fecha;
	private double latitud;
	private double longitud;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Viaje getViaje() {
		return viaje;
	}
	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
}
