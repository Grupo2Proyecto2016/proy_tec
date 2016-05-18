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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Empresa {

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id_Empresa;
	private String nombre;
	private String razon_Social;
	private String rut;
	private String telefono;
	private String direccion;
	private String nombre_Tenant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_pais", nullable=false, updatable=false)
	private Pais pais;
	
//	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)	
//    @JoinColumn(name = "id_empresa")
//    private List<Contacto> contactos;
	
	/*@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	@JoinTable(name="empresa_modulo", joinColumns={ @JoinColumn(name="id_empresa", referencedColumnName="id_empresa") },
			   inverseJoinColumns={ @JoinColumn(name="id_modulo", referencedColumnName="id_modulo", unique=true) })*/
//	@ManyToMany(targetEntity=Modulo.class)	
//	private List<Modulo> modulos;
//	
//	public List<Modulo> getModulos() {
//		return modulos;
//	}

//	public void setModulos(List<Modulo> modulos) {
//		this.modulos = modulos;
//	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais country) 
	{
		this.pais = country;
	}

	public String getRazonSocial() {
		return razon_Social;
	}

	public void setRazonSocial(String razonSocial) {
		this.razon_Social = razonSocial;
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
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombreTenant() {
		return nombre_Tenant;
	}

	public void setNombreTenant(String nombreTenant) {
		this.nombre_Tenant = nombreTenant;
	}

	public long getIdEmpresa() {
		return id_Empresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.id_Empresa = idEmpresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    public Empresa() {}
    
    public Empresa(long idEmpresa, String nombre) {
    	this.id_Empresa = idEmpresa;
        this.nombre = nombre;        
    }
    
    @Override
    public String toString() {
        return String.format(
                "Empresa[id=%d, nombre='%s']",
                id_Empresa, nombre);
    }

//	public List<Contacto> getContactos() {
//		return contactos;
//	}
//
//	public void setContactos(List<Contacto> contactos) {
//		this.contactos = contactos;
//	}
	
	

}
