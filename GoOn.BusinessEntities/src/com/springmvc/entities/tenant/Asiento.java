package com.springmvc.entities.tenant;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Asiento {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_asiento;	
	private int  numero;
	private Boolean es_ventana;
	private Boolean es_accesible;
	private Boolean habilitado;
	
	@Transient
	private Boolean reservado = false;
	
	public long getId_asiento() {
		return id_asiento;
	}
	public void setId_asiento(long id_asiento) {
		this.id_asiento = id_asiento;
	}
	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Boolean getEs_ventana() {
		return es_ventana;
	}
	public void setEs_ventana(Boolean es_ventana) {
		this.es_ventana = es_ventana;
	}
	public Boolean getEs_accesible() {
		return es_accesible;
	}
	public void setEs_accesible(Boolean es_accesible) {
		this.es_accesible = es_accesible;
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	
	public Asiento() //constructor para que funcione el json
	{
		
	}
	
	public Asiento(BigInteger id_asiento, Integer numero, Boolean es_ventana, Boolean es_accesible, Boolean habilitado)
	{
		super();
		this.id_asiento = id_asiento.longValue();	
		this.numero = numero;
		this.es_ventana = es_ventana;
		this.es_accesible = es_accesible;
		this.habilitado = habilitado;
	}
	public Boolean getReservado() {
		return reservado;
	}
	public void setReservado(Boolean reservado) {
		this.reservado = reservado;
	}
}
