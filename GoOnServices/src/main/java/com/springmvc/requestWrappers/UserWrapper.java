package com.springmvc.requestWrappers;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserWrapper 
{
	public String 	nombre;
	
	public String 	apellido;

	public String 	usrname;

	public String 	ci;
	
	public String 	passwd;
	
	public String 	email;
	
	public String 	telefono;

	public Boolean	es_empleado;
	
	public Date fch_nacimiento;
	
	public String direccion;	
	
	public int rol_id_rol;
	
	public int id_sucursal;
}
