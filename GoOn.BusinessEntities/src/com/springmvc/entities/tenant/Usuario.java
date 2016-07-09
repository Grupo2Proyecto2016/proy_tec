package com.springmvc.entities.tenant;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springmvc.enums.UserRol;

@Entity
public class Usuario {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
	private long id_usuario;
	
	private String 	usrname;
	
	@JsonIgnore
	private String 	passwd;
	
	private String 	nombre;
	
	private String 	ci;

	private String 	apellido;
	
	private String 	email;
	
	private String 	telefono;

	@JsonIgnore
	private Boolean	enabled;
	
	private Boolean	es_empleado;
	
	@JsonIgnore
	private Date ultimo_reset_password;
	
	private Date fch_nacimiento;
	
	private String direccion;	
	
	private int rol_id_rol;

	@ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;
	
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}
	
	public List<Authority> getAuthorities()
	{
		
		List<Authority> result = new ArrayList<>();
		Authority auth = new Authority();
		auth.setId(1L);
		switch (rol_id_rol) {
		case 1:
			auth.setName(AuthorityName.ROLE_ADMIN);
			break;
		case 2:
			auth.setName(AuthorityName.ROLE_SALES);
			break;
		case 3:
			auth.setName(AuthorityName.ROLE_DRIVER);
			break;
		case 4:
			auth.setName(AuthorityName.ROLE_CLIENT);
			break;
		case 5:
			auth.setName(AuthorityName.ROLE_COORDINATOR);
			break;
		default:
			auth.setName(AuthorityName.ROLE_ANONYMOUS);
			break;
		}
		result.add(auth);
		return result;
	}
	
	@Transient
	public UserRol getRol()
	{
		UserRol result = null;
		switch (rol_id_rol) 
		{
		case 1:
			result = UserRol.Admin;
			break;
		case 2:
			result = UserRol.Sales;
			break;
		case 3:
			result = UserRol.Driver;
			break;
		case 4:
			result = UserRol.Client;
			break;
		case 5:
			result = UserRol.Coordinator;
			break;
		}
		return result;
	}
	
	public void SetAuthorities(List<Authority> authorities)
	{
		//TODO: agregar cuando se agregue mapping de roles
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public Date getUltimoResetPassword() {
		return ultimo_reset_password;
	}
	public void setUltimoResetPassword(Date ultimoResetPassword) {
		this.ultimo_reset_password = ultimoResetPassword;
	}
	public long getIdUsuario() {
		return id_usuario;
	}
	public void setIdUsuario(long idUsuario) {
		this.id_usuario = idUsuario;
	}
	public String getUsrname() {
		return usrname;
	}
	public void setUsrname(String usrname) {
		this.usrname = usrname;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEs_empleado() {
		return es_empleado;
	}

	public void setEs_empleado(Boolean es_empleado) {
		this.es_empleado = es_empleado;
	}

	public Date getFch_nacimiento() {
		return fch_nacimiento;
	}

	public void setFch_nacimiento(Date fch_nacimiento) {
		this.fch_nacimiento = fch_nacimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getRol_id_rol() {
		return rol_id_rol;
	}

	public void setRol_id_rol(int rol_id_rol) {
		this.rol_id_rol = rol_id_rol;
	}

}
