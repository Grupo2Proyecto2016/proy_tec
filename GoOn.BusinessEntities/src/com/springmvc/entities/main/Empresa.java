package com.springmvc.entities.main;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Empresa {

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id_Empresa;
	private String nombre;
	private String razon_Social;
	private String rut;
	private String telefono;
	private String direccion;
	private String nombretenant;
	private String css;
	@JsonIgnore
	private byte[] logocontent;
	@Transient
	public String colorHeader;
	@Transient
	public String colorBack;
	@Transient
	public String colorText;
	@Transient
	public String colorTextHeader;

	@Transient
	public String logo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_pais", nullable=false, updatable=false)
	private Pais pais;

	public byte[] getLogocontent() {
		return logocontent;
	}

	public void setLogocontent(byte[] logocontent) {
		this.logocontent = logocontent;
	}
	
	public String getCss() {
		return css;
	}
	
	public void setCss(String css) {
		this.css = css;
	}

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
		return nombretenant;
	}

	public void setNombreTenant(String nombreTenant) {
		this.nombretenant = nombreTenant;
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
	
	public String getColorHeader() {
		return colorHeader;
	}

	public void setColorHeader(String colorHeader) {
		this.colorHeader = colorHeader;
	}
	
	public String getColorText() {
		return colorText;
	}

	public void setColorText(String colorText) {
		this.colorText = colorText;
	}
	
	public String getColorBack() {
		return colorBack;
	}

	public void setColorBack(String colorBack) {
		this.colorBack = colorBack;
	}
	
	public String getColorTextHeader() {
		return colorTextHeader;
	}

	public void setColorTextHeader(String colorTextHeader) {
		this.colorTextHeader = colorTextHeader;
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
