package com.springmvc.entities.tenant;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Vehiculo {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_vehiculo;
	private String marca;
	private String modelo;
	private String matricula;
	private int ano;
	private Boolean tieneBano;
	private int cantAsientos;
	private int cantParados;
	private int cantAccesibles;
	private int cantAnimales;
	private int cantEncomiendas; //basados en bultos de 1 m cubico
	private int cantBultos;
	private int pisos;

	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)	
    @JoinColumn(name = "id_vehiculo")
    private Collection <Asiento> asientos;
	
	public Collection<Asiento> getAsientos() {
		return asientos;
	}
	public void setAsientos(Collection<Asiento> asientos) {
		this.asientos = asientos;
	}
	public long getId_vehiculo() {
		return id_vehiculo;
	}
	public void setId_vehiculo(long id_vehiculo) {
		this.id_vehiculo = id_vehiculo;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public Boolean getTieneBano() {
		return tieneBano;
	}
	public void setTieneBano(Boolean tieneBano) {
		this.tieneBano = tieneBano;
	}
	public int getCantAsientos() {
		return cantAsientos;
	}
	public void setCantAsientos(int cantAsientos) {
		this.cantAsientos = cantAsientos;
	}
	public int getCantParados() {
		return cantParados;
	}
	public void setCantParados(int cantParados) {
		this.cantParados = cantParados;
	}
	public int getCantAccesibles() {
		return cantAccesibles;
	}
	public void setCantAccesibles(int cantAccesibles) {
		this.cantAccesibles = cantAccesibles;
	}
	public int getCantAnimales() {
		return cantAnimales;
	}
	public void setCantAnimales(int cantAnimales) {
		this.cantAnimales = cantAnimales;
	}
	public int getCantEncomiendas() {
		return cantEncomiendas;
	}
	public void setCantEncomiendas(int cantEncomiendas) {
		this.cantEncomiendas = cantEncomiendas;
	}
	public int getCantBultos() {
		return cantBultos;
	}
	public void setCantBultos(int cantBultos) {
		this.cantBultos = cantBultos;
	}
	public int getPisos() {
		return pisos;
	}
	public void setPisos(int pisos) {
		this.pisos = pisos;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}	
}
