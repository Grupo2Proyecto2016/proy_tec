package com.springmvc.enums;

import com.springmvc.entities.tenant.Rol;

public enum UserRol
{
	Admin(1),
	Sales(2),
	Driver(3),
	Client(4);

	private int value; 
		
	private UserRol(int value)
	{ 
		this.value = value; 
	}
		
	public int getValue()
	{
		return value;
	}

	public Rol ToRol()
	{
		Rol rol = new Rol(value, this.name(), this.name());
		return rol;
	}
}
