package com.springmvc.enums;

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
}
