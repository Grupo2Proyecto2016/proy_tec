package com.springmvc.entities.tenant;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Linea {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_linea;
	private int numero;
	private String origen; //deberian ser paradaS?
	private String destino;
	private Double costo_fijo;
	private int tiempo_estimado; //en minutos
	private Boolean viaja_parado;
	
	@ManyToMany(targetEntity=Parada.class)	
	private List<Parada> paradas;

	public long getId_linea() {
		return id_linea;
	}

	public void setId_linea(long id_linea) {
		this.id_linea = id_linea;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Double getCosto_fijo() {
		return costo_fijo;
	}

	public void setCosto_fijo(Double costo_fijo) {
		this.costo_fijo = costo_fijo;
	}

	public int getTiempo_estimado() {
		return tiempo_estimado;
	}

	public void setTiempo_estimado(int tiempo_estimado) {
		this.tiempo_estimado = tiempo_estimado;
	}

	public Boolean getViaja_parado() {
		return viaja_parado;
	}

	public void setViaja_parado(Boolean viaja_parado) {
		this.viaja_parado = viaja_parado;
	}

	public List<Parada> getParadas() {
		return paradas;
	}

	public void setParadas(List<Parada> paradas) {
		this.paradas = paradas;
	}
}
