package com.springmvc.entities.tenant;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Devolucion {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_devolucion;	
	private String  ci_cliente;
	private String id_pago;
	private Boolean realizado = false;
	
	public long getId_devolucion() {
		return id_devolucion;
	}
	public void setId_devolucion(long id_devolucion) {
		this.id_devolucion = id_devolucion;
	}
	public String getCi_cliente() {
		return ci_cliente;
	}
	public void setCi_cliente(String ci_cliente) {
		this.ci_cliente = ci_cliente;
	}
	public String getId_pago() {
		return id_pago;
	}
	public void setId_pago(String id_pago) {
		this.id_pago = id_pago;
	}
	public Boolean getRealizado() {
		return realizado;
	}
	public void setRealizado(Boolean realizado) {
		this.realizado = realizado;
	}

}
