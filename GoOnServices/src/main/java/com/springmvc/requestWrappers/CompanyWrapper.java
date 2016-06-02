package com.springmvc.requestWrappers;

import com.springmvc.entities.tenant.Usuario;

public class CompanyWrapper
{
	public String address;

	public long countryId;

	public String name;

	public String password;

	public String phone;

	public String rut;
	
	public String tenantName;

	public String trueName;

	public String username;
	
	public Usuario user;
	
	private double latitud;
	
	private double longitud;
	
	private boolean addTerminal;
	
	public boolean isAddTerminal() {
		return addTerminal;
	}

	public void setAddTerminal(boolean addTerminal) {
		this.addTerminal = addTerminal;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	
}
