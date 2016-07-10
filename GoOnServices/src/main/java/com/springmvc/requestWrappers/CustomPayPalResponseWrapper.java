package com.springmvc.requestWrappers;

import java.util.ArrayList;
import java.util.List;

import com.springmvc.entities.tenant.Pasaje;

public class CustomPayPalResponseWrapper 
{
	private Boolean success;
	private String	msg;
	List<Pasaje> tickets = new ArrayList<>();
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Pasaje> getTickets() {
		return tickets;
	}
	public void setTickets(List<Pasaje> tickets) {
		this.tickets = tickets;
	}
}
