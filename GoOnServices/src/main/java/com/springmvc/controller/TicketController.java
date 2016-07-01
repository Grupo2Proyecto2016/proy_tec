package com.springmvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.PackageLogic;
import com.springmvc.requestWrappers.TravelSearchWrapper;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class TicketController 
{

	@Autowired
    private UserContext context;
	
	@RequestMapping(value = "/searchTravels", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<List<Viaje>> travels(@RequestBody TravelSearchWrapper searchData, @PathVariable String tenantid)
	{
		List<Viaje> travels = new LinesLogic(tenantid).SearchTravels(searchData.dateFrom, searchData.dateTo, searchData.origins, searchData.destinations);
		return new ResponseEntity<List<Viaje>>(travels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getStations", method = RequestMethod.GET)
	public ResponseEntity<List<Parada>> getStations(@PathVariable String tenantid)
	{
		List<Parada> stations = new LinesLogic(tenantid).GetParadas();
		return new ResponseEntity<List<Parada>>(stations, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getFilteredStations", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<List<Parada>> getFilteredStations(@RequestBody List<Integer> destinations, @PathVariable String tenantid)
	{
		List<Parada> stations = new LinesLogic(tenantid).GetStationsByDestinations(destinations);
		return new ResponseEntity<List<Parada>>(stations, HttpStatus.OK);
	}		
	
	@Secured({"ROLE_CLIENT"})
	@RequestMapping(value = "/userTickets", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Pasaje>> getUserTickets(@PathVariable String tenantid, HttpServletRequest request) throws Exception
	{
		Usuario currentUser = context.GetUser(request, tenantid);
		LinesLogic ll = new LinesLogic(tenantid);
		List<Pasaje> tickets = ll.GetUserTickets(currentUser);

		return new ResponseEntity<List<Pasaje>>(tickets, HttpStatus.OK);
	}
}
