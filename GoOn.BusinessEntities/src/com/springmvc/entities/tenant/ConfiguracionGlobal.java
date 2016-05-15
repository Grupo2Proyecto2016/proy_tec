package com.springmvc.entities.tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ConfiguracionGlobal {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_configuracion;
	public double precio_km;
	public double precio_peaje;
	public int minuto_retraso;
	
	public double getPrecio_km() {
		return precio_km;
	}
	public void setPrecio_km(double precio_km) {
		this.precio_km = precio_km;
	}
	public double getPrecio_peaje() {
		return precio_peaje;
	}
	public void setPrecio_peaje(double precio_peaje) {
		this.precio_peaje = precio_peaje;
	}
	public int getMinuto_retraso() {
		return minuto_retraso;
	}
	public void setMinuto_retraso(int minuto_retraso) {
		this.minuto_retraso = minuto_retraso;
	}
}
