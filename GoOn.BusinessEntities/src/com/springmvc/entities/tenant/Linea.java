package com.springmvc.entities.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Linea {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_linea;
	private int numero;
	
	@ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_parada_origen")
	private Parada origen; 
	
	@ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_parada_destino")
	private Parada destino;
	
	private Double costo_maximo;
	private Double costo_minimo;
	private int tiempo_estimado; //en minutos
	private Boolean viaja_parado = false;
	private Boolean habilitado;
	
	@ManyToMany(targetEntity=Parada.class, cascade = {CascadeType.ALL})	
	private List<Parada> paradas = new ArrayList<Parada>();

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

	public Parada getOrigen() {
		return origen;
	}

	public void setOrigen(Parada origen) {
		this.origen = origen;
	}

	public Parada getDestino() {
		return destino;
	}

	public void setDestino(Parada destino) {
		this.destino = destino;
	}

	public Double getCosto_maximo() {
		return costo_maximo;
	}

	public void setCosto_maximo(Double costo_maximo) {
		this.costo_maximo = costo_maximo;
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

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Double getCosto_minimo() {
		return costo_minimo;
	}

	public void setCosto_minimo(Double costo_minimo) {
		this.costo_minimo = costo_minimo;
	}
}
