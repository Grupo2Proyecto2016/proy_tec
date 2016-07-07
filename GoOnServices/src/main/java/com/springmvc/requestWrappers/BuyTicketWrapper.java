package com.springmvc.requestWrappers;

import java.util.ArrayList;
import java.util.List;

public class BuyTicketWrapper 
{
	public long id_viaje;
	public int origen;
	public int destino;
	public int id_linea;
	public int id_vehiculo;
	public Double valor;
	public List<Long> seleccionados = new ArrayList<>();	
}
