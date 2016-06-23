package com.springmvc.entities.tenant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Encomienda {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_encomienda;
	private String descripcion;
	private Double peso;
	private Double volumen;
	private Date Fecha;
	private String nombre_destinatario;
	private String ci_destinatario;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Usuario usr_envia;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Usuario usr_recibe;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Usuario usr_crea;
	
	public long getId_encomienda() {
		return id_encomienda;
	}
	public void setId_encomienda(long id_encomienda) {
		this.id_encomienda = id_encomienda;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Double getVolumen() {
		return volumen;
	}
	public void setVolumen(Double volumen) {
		this.volumen = volumen;
	}
	public Date getFecha() {
		return Fecha;
	}
	public void setFecha(Date fecha) {
		Fecha = fecha;
	}
	public String getNombre_destinatario() {
		return nombre_destinatario;
	}
	public void setNombre_destinatario(String nombre_destinatario) {
		this.nombre_destinatario = nombre_destinatario;
	}
	public String getCi_destinatario() {
		return ci_destinatario;
	}
	public void setCi_destinatario(String ci_destinatario) {
		this.ci_destinatario = ci_destinatario;
	}
	public Usuario getUsr_envia() {
		return usr_envia;
	}
	public void setUsr_envia(Usuario usr_envia) {
		this.usr_envia = usr_envia;
	}
	public Usuario getUsr_recibe() {
		return usr_recibe;
	}
	public void setUsr_recibe(Usuario usr_recibe) {
		this.usr_recibe = usr_recibe;
	}
	public Usuario getUsr_crea() {
		return usr_crea;
	}
	public void setUsr_crea(Usuario usr_crea) {
		this.usr_crea = usr_crea;
	}
}
