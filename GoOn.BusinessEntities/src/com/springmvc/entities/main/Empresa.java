package com.springmvc.entities.main;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Empresa {

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long idEmpresa;
	private String nombre;
	private String razonSocial;
	private String rut;
	private String telefono;
	private String pais;
	private String direccion;
	private String nombreTenant;
	
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombreTenant() {
		return nombreTenant;
	}

	public void setNombreTenant(String nombreTenant) {
		this.nombreTenant = nombreTenant;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    public Empresa() {}
    
    public Empresa(long idEmpresa, String nombre) {
    	this.idEmpresa = idEmpresa;
        this.nombre = nombre;        
    }
    
    @Override
    public String toString() {
        return String.format(
                "Empresa[id=%d, nombre='%s']",
                idEmpresa, nombre);
    }
	
	

}
