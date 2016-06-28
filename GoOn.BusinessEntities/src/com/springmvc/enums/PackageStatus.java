package com.springmvc.enums;

public enum PackageStatus 
{
	Created(1),
	Transported(2),
	Delivered(3);
	
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
