package com.springmvc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Viaje;
import com.springmvc.logic.implementations.LinesLogic;

@RestController
@RequestMapping(value = "/{tenantid}")
public class TicketController 
{

	@RequestMapping(value = "/travels", method = RequestMethod.GET)
	public ResponseEntity<List<Viaje>> travels(@PathVariable String tenantid)
	{
		List<Viaje> travels = new LinesLogic(tenantid).GetTravels();
		return new ResponseEntity<List<Viaje>>(travels, HttpStatus.OK);
	}
}
