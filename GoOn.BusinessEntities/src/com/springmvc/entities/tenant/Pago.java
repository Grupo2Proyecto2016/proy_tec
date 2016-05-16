package com.springmvc.entities.tenant;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pago {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_pago;
	private long id_pasaje; //apunta a pasaje, cambiar
	private Date fecha; 
	private int metodo; //apunta a metodos? constantes?
	private Double valor;
	
	public long getId_pago() {
		return id_pago;
	}
	public void setId_pago(long id_pago) {
		this.id_pago = id_pago;
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
