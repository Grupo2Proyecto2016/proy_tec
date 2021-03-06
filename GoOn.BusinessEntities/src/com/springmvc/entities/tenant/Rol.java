package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rol 
{
	@Id
	public long id_rol;
	public String nombre;
	public String descripcion;
	
	public Rol(long idRol, String name, String description)
	{
		this.id_rol = idRol;
		this.nombre = name;
		this.descripcion = description;
	}
	
	public long getId_rol() {
		return id_rol;
	}
	public void setId_rol(long id_rol) {
		this.id_rol = id_rol;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
