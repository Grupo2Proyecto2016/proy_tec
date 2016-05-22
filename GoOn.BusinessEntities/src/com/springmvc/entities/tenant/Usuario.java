package com.springmvc.entities.tenant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
	private long id_usuario;
	
	@Column(unique=true)
	private String 	usrname;
	
	@JsonIgnore
	private String 	passwd;
	
	private String 	nombre;
	
	private String 	apellido;
	
	private String 	email;
	
	@JsonIgnore
	private Boolean	puede_crear;
	
	@JsonIgnore
	private Boolean	enabled;
	
	private Boolean	es_empleado;
	
	@JsonIgnore
	private Date ultimo_reset_password;
	
	private Date fch_nacimiento;
	
	private String direccion;	
	
	private int rol_id_rol;

	
	public List<Authority> getAuthorities()
	{
		List<Authority> result = new ArrayList<>();
		Authority auth = new Authority();
		auth.setId(1L);
		auth.setName(AuthorityName.ROLE_ADMIN);
		result.add(auth);
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
	public Boolean getPuede_crear() {
		return puede_crear;
	}
	public void setPuede_crear(Boolean puede_crear) {
		this.puede_crear = puede_crear;
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
