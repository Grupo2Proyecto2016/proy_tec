package com.springmvc.requestWrappers;

import java.util.List;

import com.springmvc.entities.tenant.Parada;

public class LinesWrapper 
{
	public long id_linea;
	public int numero;
	public String origen; 
	public String destino;
	public Double costo_fijo;
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
}
