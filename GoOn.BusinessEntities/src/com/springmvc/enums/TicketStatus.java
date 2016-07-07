package com.springmvc.enums;

public enum TicketStatus 
{
	Reserved(1),
	Bought(2),
	InTravel(3),
	cashed(4);
	
	private int value; 
	
	private TicketStatus(int value)
	{ 
		this.value = value; 
	}
	
	public int getValue()
	{
		return value;
	}
}
