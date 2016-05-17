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
public class Pais {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_pais;
	private String nombre;
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)	
    @JoinColumn(name = "id_pais")
    private List<Departamento> departamentos;
	
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
	public List<Departamento> getDepartamentos() {
		return departamentos;
	}
	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}
}
