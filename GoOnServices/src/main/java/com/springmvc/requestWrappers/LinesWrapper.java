package com.springmvc.requestWrappers;

import java.util.List;

import com.springmvc.entities.tenant.Parada;

public class LinesWrapper 
{
	public long id_linea;
	public int numero;
	public String origen; 
	public String destino;
	public Double costo_maximo;
	public Double costo_minimo;
	public int tiempo_estimado; 
	public Boolean viaja_parado;
	
	private List<Parada> paradas;
	
	
	public List<Parada> getParadas() {
		return paradas;
	}
	public void setParadas(List<Parada> paradas) {
		this.paradas = paradas;
	}
	
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
	public Double getCosto_minimo() {
		return costo_minimo;
	}
	public void setCosto_minimo(Double costo_minimo) {
		this.costo_minimo = costo_minimo;
	}
}
