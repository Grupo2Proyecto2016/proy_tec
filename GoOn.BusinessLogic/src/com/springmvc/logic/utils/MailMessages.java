package com.springmvc.logic.utils;

public class MailMessages 
{
	public static String AppSite = "http://localhost:8080/GoOnSite/";
	
	public static String TenantCreatedTitle = "La plataforma esta lista!";
	public static String TenantCreatedMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "La plataforma est� lista para ser configurada. " + System.lineSeparator()
		+ "Tu nombre de usuario es %s. Por razones de seguridad no incluimos contrase�as en los correos." + System.lineSeparator()
		+ "Visita "+AppSite+"%s para emprezar a configurar tu negocio"
	;
	
	public static String EmployeeCreatedTitle = "Tu usuario est� listo!";
	public static String EmployeeCreatedMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu usuario est� listo para que realices tus tareas. " + System.lineSeparator()
		+ "El nombre de usuario es %s. Por razones de seguridad no incluimos contrase�as en los correos." + System.lineSeparator()
		+ "Visita "+AppSite+"%s para emprezar a desempe�ar tus tareas" + System.lineSeparator()
		+ "Por cualquier consulta contacta al personal de recursos humanos";
	;
	
	public static String ClientCreatedTitle = "Gracias por elegirnos!";
	public static String ClientCreatedMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu usuario est� listo para hacer uso de nuestros servicios de transporte y encomiendas. " + System.lineSeparator()
		+ "El nombre de usuario es %s. Por razones de seguridad no incluimos contrase�as en los correos." + System.lineSeparator()
		+ "Te esperamos en "+AppSite+"%s para emprezar a hacer uso de tu cuenta" + System.lineSeparator()
	;
}
