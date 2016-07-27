package com.springmvc.entities.tenant;

import java.util.ArrayList;
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
import javax.persistence.Transient;

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
	private int cantAccesibles;
	private int cantEncomiendas; //basados en bultos de 1 m cubico
	
	@Transient
	private boolean puedeBorrarse; //Atributo que no es persistido es solo para utilizar en tiempo de ejecucion
	
	@OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_vehiculo")
    private List<Asiento> asientos = new ArrayList<Asiento>();
	
	public List<Asiento> getAsientos() {
		return asientos;
	}
	public void setAsientos(List<Asiento> asientos) {
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
	public int getCantAccesibles() {
		return cantAccesibles;
	}
	public void setCantAccesibles(int cantAccesibles) {
		this.cantAccesibles = cantAccesibles;
	}
	public int getCantEncomiendas() {
		return cantEncomiendas;
	}
	public void setCantEncomiendas(int cantEncomiendas) {
		this.cantEncomiendas = cantEncomiendas;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public boolean isPuedeBorrarse() {
		return puedeBorrarse;
	}
	public void setPuedeBorrarse(boolean puedeBorrarse) {
		this.puedeBorrarse = puedeBorrarse;
	}
}
