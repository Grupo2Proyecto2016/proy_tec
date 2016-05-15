package com.springmvc.entities.tenant;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pago {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_usuario;
	private long id_pasaje; //apunta a pasaje, cambiar
	private Date fecha; 
	private int metodo; //apunta a metodos? constantes?
	private Double valor;
	
	public long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	public long getId_pasaje() {
		return id_pasaje;
	}
	public void setId_pasaje(long id_pasaje) {
		this.id_pasaje = id_pasaje;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getMetodo() {
		return metodo;
	}
	public void setMetodo(int metodo) {
		this.metodo = metodo;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
}
