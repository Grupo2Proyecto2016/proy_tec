package com.springmvc.enums;


public enum Parameter 
{
	PriceByKg(1),
	PriceByVolume(2),
	PriceByTravelKm(3),
	PriceByPackageKm(4),
	MaxReservationDelay(5),
	StopDelay(6),
	PackageBasePrice(7);
	
	private int value; 
	
	private Parameter(int value)
	{ 
		this.value = value; 
	}
	
	public int getValue()
	{
		return value;
	}
	
}
