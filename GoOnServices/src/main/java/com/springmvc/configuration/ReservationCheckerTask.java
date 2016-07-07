package com.springmvc.configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.springmvc.entities.tenant.Parametro;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.enums.Parameter;
import com.springmvc.enums.TicketStatus;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.ParametersLogic;

public class ReservationCheckerTask extends Thread
{
	String tenantName;
	int minutesLimit;
	
	@SuppressWarnings("restriction")
	public ReservationCheckerTask(String tenant)
	{
		this.tenantName = tenant;
		Parametro cancelationTime = new ParametersLogic(tenantName).FindById(Parameter.MaxReservationDelay.getValue());
		minutesLimit = (int)cancelationTime.getValor();
	}
	
	public void run()
    {
    	try 
    	{
    		LinesLogic ll = new LinesLogic(tenantName);
    		Calendar now = Calendar.getInstance();
    		now.add(GregorianCalendar.MINUTE, minutesLimit);
    		List<Pasaje> reservedTickets = ll.GetTicketsByStatusAndTime(TicketStatus.Reserved, now.getTime());
    		for (Pasaje ticket : reservedTickets) 
    		{
    			ll.DeleteTicket(ticket);
			}
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
}
