package com.springmvc.logic.utils;

public class MailSender
{
	public static void Send(String recipientEmail, String title, String message){
		MailSender.Send(recipientEmail, "", title, message);
    }
	
	public static void Send(String recipientEmail, String ccEmail, String title, String message) 
	{
		new GoogleMail(recipientEmail, ccEmail, title, message).start();
	}
}
