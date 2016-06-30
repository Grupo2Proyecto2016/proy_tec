package com.springmvc.enums;

public enum PackageStatus 
{
	Created(1),
	Carring(2),
	Transported(3),
	Delivered(4);
	
	private int value; 
	
	private PackageStatus(int value)
	{ 
		this.value = value; 
	}
	
	public int getValue()
	{
		return value;
	}
}
