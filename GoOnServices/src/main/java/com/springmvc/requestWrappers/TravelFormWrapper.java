package com.springmvc.requestWrappers;

import java.util.Calendar;
import java.util.Date;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Vehiculo;

public class TravelFormWrapper
{
	public Linea line;
	
	public Usuario driver;
	
	public Vehiculo bus;
	
	public String time;
	
	public boolean monday;
	public boolean tuesday;
	public boolean wednesday;
	public boolean thursday;
	public boolean friday;
	public boolean saturday;
	public boolean sunday;
	
	public Calendar dayFrom;
	public Calendar dayTo;
}
