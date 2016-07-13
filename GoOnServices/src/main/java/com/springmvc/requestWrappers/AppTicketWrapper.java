package com.springmvc.requestWrappers;

import java.util.ArrayList;
import java.util.List;

import com.springmvc.entities.tenant.Pasaje;

public class AppTicketWrapper 
{
	public String id_Pago;
	public List<Pasaje> tickets = new ArrayList<>();
}
