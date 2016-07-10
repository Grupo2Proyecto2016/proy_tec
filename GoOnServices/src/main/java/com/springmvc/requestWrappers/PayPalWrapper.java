package com.springmvc.requestWrappers;

import java.util.ArrayList;
import java.util.List;

public class PayPalWrapper 
{
	
	public String paymentId;
	public String token;
	public String PayerID;
	
	public long id_viaje;
	public int origen;
	public int destino;
	public int id_linea;
	public int id_vehiculo;
	public String rDoc;
	public String rUser;
	public Double valor;
	public List<Long> seleccionados = new ArrayList<>();
}
