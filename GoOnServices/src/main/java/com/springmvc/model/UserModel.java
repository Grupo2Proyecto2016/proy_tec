package com.springmvc.model;

import java.util.Date;

import com.springmvc.entities.main.Usuario;

public class UserModel {

	private long id;
	
	private String username;
	
	private String password;

	private String firstname;

    private String lastname;
	
	private String address;
	
	private String email;
	
	private Boolean enabled;
	
	private Date lastPasswordResetDate;

	
	public UserModel(){
		id=0;
	}
	
	public UserModel(long id, String username, String address, String email){
		this.id = id;
		this.username = username;
		this.address = address;
		this.email = email;
	}

	public UserModel(Usuario user)
	{
		this.id = user.getIdUsuario();
		this.username = user.getUsrname();
		this.address = null;
		this.email = user.getEmail();
	}
	
	public Usuario ToEntity()
	{
		Usuario userEntity = new Usuario();
		//userEntity.setAddress(address);
		userEntity.SetAuthorities(null);
		userEntity.setEmail(email);
		userEntity.setEnabled(true);
		userEntity.setNombre(firstname);
		userEntity.setApellido(lastname);
		userEntity.setUltimoResetPassword(lastPasswordResetDate);
		userEntity.setPasswd(password);
		userEntity.setUsrname(username);
		
		return userEntity;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserModel))
			return false;
		UserModel other = (UserModel) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", address=" + address
				+ ", email=" + email + "]";
	}
	

	
}
