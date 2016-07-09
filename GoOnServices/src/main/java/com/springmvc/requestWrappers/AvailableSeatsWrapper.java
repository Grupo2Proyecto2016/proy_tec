package com.springmvc.requestWrappers;

import java.util.List;

import com.springmvc.entities.tenant.Asiento;

public class AvailableSeatsWrapper 
{
	public boolean success;
	public String msg;
	public List<Asiento> seats;
	public double cost;
}
