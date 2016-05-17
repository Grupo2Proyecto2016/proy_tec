package com.springmvc.entities.main;



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
public class Departamento 
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_departamento;
	private long id_pais;
	private String nombre;
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)	
	@JoinColumn(name = "id_departamento")
    private List<Ciudad> ciudades;
	
	public long getId_departamento() {
		return id_departamento;
	}
	public void setId_departamento(long id_departamento) {
		this.id_departamento = id_departamento;
	}
	public long getId_pais() {
		return id_pais;
	}
	public void setId_pais(long id_pais) {
		this.id_pais = id_pais;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Ciudad> getCiudades() {
		return ciudades;
	}
	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}
}
