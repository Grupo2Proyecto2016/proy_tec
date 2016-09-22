package com.springmvc.logic.utils;

public class MailMessages 
{
	public static String AppSite = "http://localhost:8080/GoOnSite/";
	
	public static String TenantCreatedTitle = "La plataforma está lista!";
	public static String TenantCreatedMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "La plataforma está lista para ser configurada. " + System.lineSeparator()
		+ "Tu nombre de usuario es %s. Por razones de seguridad no incluimos contraseñas en los correos." + System.lineSeparator()
		+ "Visita "+AppSite+"%s para emprezar a configurar tu negocio"
	;
	
	public static String EmployeeCreatedTitle = "Tu usuario está listo!";
	public static String EmployeeCreatedMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu usuario está listo para que realices tus tareas. " + System.lineSeparator()
		+ "El nombre de usuario es %s. Por razones de seguridad no incluimos contraseñas en los correos." + System.lineSeparator()
		+ "Visita "+AppSite+"%s para emprezar a desempeñar tus tareas" + System.lineSeparator()
		+ "Por cualquier consulta contacta al personal de recursos humanos";
	;
	
	public static String ClientCreatedTitle = "Gracias por elegirnos!";
	public static String ClientCreatedMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu usuario está listo para hacer uso de nuestros servicios de transporte y encomiendas. " + System.lineSeparator()
		+ "El nombre de usuario es %s. Por razones de seguridad no incluimos contraseñas en los correos." + System.lineSeparator()
		+ "Te esperamos en "+AppSite+"%s para emprezar a hacer uso de tu cuenta" + System.lineSeparator()
	;
	
	public static String TicketCancelationTitle = "Tu pasaje ha sido cancelado";
	public static String TicketCancelationMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "La cancelación de tu pasaje ha sido confirmada." + System.lineSeparator()
		+ "Lamentamos que hayas tenido que cancelar tu viaje y te esperamos pronto para guiarte a nuevos destinos." + System.lineSeparator()
		+ "Nos vemos pronto!" + System.lineSeparator()
	;
	
	public static String TicketReservationTitle = "Tu pasaje ha sido reservado";
	public static String TicketReservationMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu pasaje ha sido reservado." + System.lineSeparator()
		+ "Para ver tus pasajes y encomiendas puedes ingresar a tu panel personal desde nuestro sitio a nuestra App." + System.lineSeparator()
		+ "Recuerda que debes confirmar la compra de tu pasaje con anticipación" + System.lineSeparator()
		+ "Nos vemos pronto!" + System.lineSeparator()
	;
	
	public static String TicketBuyTitle = "Gracias por tu compra";
	public static String TicketBuyMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Ya has adquirido tu pasaje." + System.lineSeparator()
		+ "Para ver tus pasajes y encomiendas puedes ingresar a tu panel personal desde nuestro sitio a nuestra App." + System.lineSeparator()
		+ "Recuerda que debes presentar el código QR al subir al ómnibus en forma impresa o desde la pantalla de tu smartphone." + System.lineSeparator()
		+ "Que tengas un excelente viaje!" + System.lineSeparator()
	;
	
	public static String PackageCarringTitle = "Tu encomienda esta en camino";
	public static String PackageCarringMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu packete esta siendo enviado en estos momentos." + System.lineSeparator()
		+ "Si quieren monitorear el packete paso a paso puedes hacer ingresando a tu panel desde nuestro sitio o App." + System.lineSeparator()
		+ "Tranquilo, cuidaremos de él!" + System.lineSeparator()
	;
	
	public static String PackageArrivedRTitle = "Ya puedes retirar tu encomienda";
	public static String PackageArrivedRMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu packete esta pronto para ser retirado." + System.lineSeparator()
		+ "Por motivos de seguridad te pedimos que ingreses a nuestra pagina o App para mas detalles sobre el retiro de tu encomienda." + System.lineSeparator()
		+ "Recuerda llevar tu documento de identidad ya que es necesario para retirar tu paquete." + System.lineSeparator()
		+ "Que disfrutes tu paquete!" + System.lineSeparator()
	;
	
	public static String PackageArrivedSTitle = "Tu encomienda ha llegado a destino";
	public static String PackageArrivedSMsg = "%s," + System.lineSeparator() + System.lineSeparator()
		+ "Tu packete ha llegado a su destino." + System.lineSeparator()
		+ "Por motivos de seguridad te pedimos que ingreses a nuestra pagina o App para mas detalles sobre tu encomienda." + System.lineSeparator()
		+ "Te esperamos pronto!" + System.lineSeparator()
	;
}
