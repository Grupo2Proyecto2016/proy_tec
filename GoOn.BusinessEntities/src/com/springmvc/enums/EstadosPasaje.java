package com.springmvc.enums;


public enum EstadosPasaje 
{
	Reservado(1),
	Pago(2),
	Cancelado(3),
	Consumido(4);
	
	private int value; 
	
	private EstadosPasaje(int value)
	{ 
		this.value = value; 
	}
	
	public int getValue()
	{
		return value;
	}
	
}
