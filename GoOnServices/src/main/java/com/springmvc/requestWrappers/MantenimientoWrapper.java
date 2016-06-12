package com.springmvc.requestWrappers;

import java.util.Date;
import com.springmvc.entities.tenant.Taller;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Vehiculo;

public class MantenimientoWrapper 
{
	private long id_mantenimiento;
	private Date fecha;
	private Double costo;
	private int estado;
	private Taller taller;
	private Vehiculo vehiculo;
	private Usuario user_crea;
	
	
	
	public long getId_mantenimiento() {
		return id_mantenimiento;
	}
	public void setId_mantenimiento(long id_mantenimiento) {
		this.id_mantenimiento = id_mantenimiento;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
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
