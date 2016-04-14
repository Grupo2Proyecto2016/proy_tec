package com.dataAccess;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Empresa{
	
	@Id    
    private long idEmpresa;
	private String nombre;
	
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

    protected Empresa() {}
    
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
