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
	private float peso;
	private float volumen;
	private String ci_emisor;
	private String ci_receptor;
	private int precio;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuario usr_envia;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuario usr_recibe;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuario usr_crea;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Viaje viaje;
	
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public Viaje getViaje() {
		return viaje;
	}
	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}
	public String getCi_receptor() {
		return ci_receptor;
	}
	public void setCi_receptor(String ci_receptor) {
		this.ci_receptor = ci_receptor;
	}
	public long getId_encomienda() {
		return id_encomienda;
	}
	public void setId_encomienda(long id_encomienda) {
		this.id_encomienda = id_encomienda;
	}
	public float getPeso() {
		return peso;
	}
	public void setPeso(float peso) {
		this.peso = peso;
	}
	public float getVolumen() {
		return volumen;
	}
	public void setVolumen(float volumen) {
		this.volumen = volumen;
	}
	public String getCi_emisor() {
		return ci_emisor;
	}
	public void setCi_emisor(String ci_emisor) {
		this.ci_emisor = ci_emisor;
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
