package com.springmvc.entities.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_usuario;
	private String 	usrname;
	private String 	passwd;
	private String 	nombre;
	private String 	apellido;
	private String 	email;
	private Boolean	puede_crear;
	private Boolean	enabled;
	private Date ultimo_reset_password;
	
	
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
}
