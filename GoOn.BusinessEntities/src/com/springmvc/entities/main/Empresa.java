package com.springmvc.entities.main;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)	
    @JoinColumn(name = "id_empresa")
    private List<Contacto> contactos;
	
	/*@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	@JoinTable(name="empresa_modulo", joinColumns={ @JoinColumn(name="id_empresa", referencedColumnName="id_empresa") },
			   inverseJoinColumns={ @JoinColumn(name="id_modulo", referencedColumnName="id_modulo", unique=true) })*/
	@ManyToMany(targetEntity=Modulo.class)	
	private List<Modulo> modulos;
	
	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

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

	public List<Contacto> getContactos() {
		return contactos;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
	
	

}
