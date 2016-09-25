package com.springmvc.requestWrappers;

import java.util.Calendar;
import java.util.Date;
import com.springmvc.entities.tenant.Taller;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Vehiculo;

public class MantenimientoFormWrapper 
{
	public long id_mantenimiento;
	public Calendar dayFrom;
	public Calendar dayTo;
	public Double costo;
	public String descripcion;
	public Taller taller;
	public Vehiculo vehiculo;
	public Usuario user_crea;
	public byte[] facturaContent;
	public String factura;
	

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public byte[] getFacturaContent() {
		return facturaContent;
	}
	public void setFacturaContent(byte[] facturaContent) {
		this.facturaContent = facturaContent;
	}
	public long getId_mantenimiento() {
		return id_mantenimiento;
	}
	public void setId_mantenimiento(long id_mantenimiento) {
		this.id_mantenimiento = id_mantenimiento;
	}
	public Calendar getFechaInicio() {
		return dayFrom;
	}
	public void setFechaInicio(Calendar dayFrom) {
		this.dayFrom = dayFrom;
	}
	public Calendar getFechaFin() {
		return dayTo;
	}
	public void setFechaFin(Calendar dayTo) {
		this.dayTo = dayTo;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Taller getTaller() {
		return taller;
	}
	public void setTaller(Taller taller) {
		this.taller = taller;
	}
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	public Usuario getUser_crea() {
		return user_crea;
	}
	public void setUser_crea(Usuario user_crea) {
		this.user_crea = user_crea;
	}
}
