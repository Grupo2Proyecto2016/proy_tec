package com.springmvc.entities.tenant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Mantenimiento {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_mantenimiento;
	private Date inicio;
	private Date fin;
	private Double costo;
	@ManyToOne(fetch=FetchType.EAGER)
	private Taller taller;
	@ManyToOne(fetch=FetchType.EAGER)
	private Vehiculo vehiculo;
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuario user_crea;
	@JsonIgnore
	private byte[] facturaContent;
	
	@Transient
	public String factura;
	
	public long getId_mantenimiento() {
		return id_mantenimiento;
	}
	public void setId_mantenimiento(long id_mantenimiento) {
		this.id_mantenimiento = id_mantenimiento;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Taller getTaller() {
		return taller;
	}
	public void setTaller(Taller taller) {
		this.taller = taller;
	}
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	public Usuario getUser_crea() {
		return user_crea;
	}
	public void setUser_crea(Usuario user_crea) {
		this.user_crea = user_crea;
	}
	
	public byte[] getFacturaContent() {
		return facturaContent;
	}
	public void setFacturaContent(byte[] facturaContent) {
		this.facturaContent = facturaContent;
	}

}
