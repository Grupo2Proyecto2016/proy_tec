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

import com.springmvc.entities.main.Contacto;

@Entity
public class Viaje {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_viaje;
	private Date fecha;
	private Date inicio;
	private Date fin;
	private Boolean es_directo;
	private String desde;
	private String hasta;
	@ManyToOne(fetch=FetchType.LAZY)
	private Linea linea;
	@ManyToOne(fetch=FetchType.LAZY)
	private Vehiculo vehiculo;
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)   
	@JoinColumn(name = "id_viaje")
    private List<Encomienda> encomiendas;
	
	public long getId_viaje() {
		return id_viaje;
	}
	public void setId_viaje(long id_viaje) {
		this.id_viaje = id_viaje;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	public String getDesde() {
		return desde;
	}
	public void setDesde(String desde) {
		this.desde = desde;
	}
	public String getHasta() {
		return hasta;
	}
	public void setHasta(String hasta) {
		this.hasta = hasta;
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
	public List<Encomienda> getEncomiendas() {
		return encomiendas;
	}
	public void setEncomiendas(List<Encomienda> encomiendas) {
		this.encomiendas = encomiendas;
	}	
}
