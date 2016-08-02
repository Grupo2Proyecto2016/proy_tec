package com.springmvc.entities.tenant;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ViajesBuscados {
	
	private long id_viaje;
	private int lugares;
	private Date inicio;
	private int numero;
	private long linea_id_linea;
	private long origen;
	private String origen_description; 
	private long destino;
	private String destino_description;
	private long numerov;
	private long id_vehiculo;
	private int cantasientos;
	private Double valor;
	
	public ViajesBuscados(BigInteger id_viaje, Integer lugares, Timestamp inicio, Integer numero, BigInteger linea_id_linea, BigInteger origen,
			String origen_description, BigInteger destino, String destino_description, BigInteger numerov, BigInteger id_vehiculo, Integer cantasientos) 
	{
		super();
		this.id_viaje = id_viaje.longValue();
		this.lugares = lugares;
		this.inicio = inicio;
		this.numero = numero;
		this.linea_id_linea = linea_id_linea.longValue();
		this.origen = origen.longValue();
		this.origen_description = origen_description;
		this.destino = destino.longValue();
		this.destino_description = destino_description;
		this.numerov = numerov.longValue();
		this.id_vehiculo = id_vehiculo.longValue();
		this.cantasientos = cantasientos;
	}
	
	public long getId_vehiculo() {
		return id_vehiculo;
	}

	public void setId_vehiculo(long id_vehiculo) {
		this.id_vehiculo = id_vehiculo;
	}

	public long getId_viaje() {
		return id_viaje;
	}
	public void setId_viaje(long id_viaje) {
		this.id_viaje = id_viaje;
	}
	public int getLugares() {
		return lugares;
	}
	public void setLugares(int lugares) {
		this.lugares = lugares;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public long getLinea_id_linea() {
		return linea_id_linea;
	}
	public void setLinea_id_linea(long linea_id_linea) {
		this.linea_id_linea = linea_id_linea;
	}
	public long getOrigen() {
		return origen;
	}
	public void setOrigen(long origen) {
		this.origen = origen;
	}
	public String getOrigen_description() {
		return origen_description;
	}
	public void setOrigen_description(String origen_description) {
		this.origen_description = origen_description;
	}
	public long getDestino() {
		return destino;
	}
	public void setDestino(long destino) {
		this.destino = destino;
	}
	public String getDestino_description() {
		return destino_description;
	}
	public void setDestino_description(String destino_description) {
		this.destino_description = destino_description;
	}

	public long getNumerov() {
		return numerov;
	}

	public void setNumerov(long numerov) {
		this.numerov = numerov;
	}

	public int getCantasientos() {
		return cantasientos;
	}

	public void setCantasientos(int cantasientos) {
		this.cantasientos = cantasientos;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	
	
}
