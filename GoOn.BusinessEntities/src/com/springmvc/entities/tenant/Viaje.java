package com.springmvc.entities.tenant;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Viaje {
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_viaje;
	private Date inicio;
	private Date fin;
	private Boolean es_directo;
	private Boolean terminado;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Linea linea;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuario conductor;

	@ManyToOne(fetch=FetchType.EAGER)
	private Vehiculo vehiculo;

	@Transient
	public int asientosLibres;
	
	public long getId_viaje() {
		return id_viaje;
	}
	public void setId_viaje(long id_viaje) {
		this.id_viaje = id_viaje;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Boolean getEs_directo() {
		return es_directo;
	}
	public void setEs_directo(Boolean es_directo) {
		this.es_directo = es_directo;
	}
	public Usuario getConductor() {
		return conductor;
	}
	public void setConductor(Usuario conductor) {
		this.conductor = conductor;
	}
	public Linea getLinea() {
		return linea;
	}
	public void setLinea(Linea linea) {
		this.linea = linea;
	}
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	public Boolean getTerminado() {
		return terminado;
	}
	public void setTerminado(Boolean terminado) {
		this.terminado = terminado;
	}	
}
