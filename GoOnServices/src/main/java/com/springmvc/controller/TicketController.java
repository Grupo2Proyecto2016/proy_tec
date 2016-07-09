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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Asiento;
import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.entities.tenant.ViajesBuscados;
import com.springmvc.exceptions.CollectTicketException;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.PackageLogic;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.requestWrappers.BuyTicketWrapper;
import com.springmvc.requestWrappers.CollectTicketWrapper;
import com.springmvc.requestWrappers.CustomResponseWrapper;
import com.springmvc.requestWrappers.TravelSearchWrapper;
import com.springmvc.requestWrappers.seatsFormWrapper;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class TicketController 
{

	@Autowired
    private UserContext context;
	
	@RequestMapping(value = "/searchTravels", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<List<ViajesBuscados>> travels(@RequestBody TravelSearchWrapper searchData, @PathVariable String tenantid)
	{
		List<ViajesBuscados> travels = new LinesLogic(tenantid).SearchTravelsAdvanced(searchData.dateFrom, searchData.dateTo, searchData.origins, searchData.destinations); 
		return new ResponseEntity<List<ViajesBuscados>>(travels, HttpStatus.OK);
	}
	
	/*@RequestMapping(value = "/searchTravels", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<List<Viaje>> travels(@RequestBody TravelSearchWrapper searchData, @PathVariable String tenantid)
	{
		List<Viaje> travels = new LinesLogic(tenantid).SearchTravels(searchData.dateFrom, searchData.dateTo, searchData.origins, searchData.destinations);
		return new ResponseEntity<List<Viaje>>(travels, HttpStatus.OK);
	}
	 */
	
	@RequestMapping(value = "/getSeats", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<List<Asiento>> getSeats(@RequestBody seatsFormWrapper searchData, @PathVariable String tenantid)
	{
		List<Asiento> asientos = new LinesLogic(tenantid).getSeats(searchData.id_viaje, searchData.id_linea, searchData.origen, searchData.destino, searchData.id_vehiculo); 
		return new ResponseEntity<List<Asiento>> (asientos, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/getTicketValue", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<Double> getTicketValue(@RequestBody seatsFormWrapper searchData, @PathVariable String tenantid)
	{
		Double value = new LinesLogic(tenantid).getTicketValue(searchData.origen, searchData.destino, searchData.id_linea);				
		return new ResponseEntity<Double> (value, HttpStatus.OK);		
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
	
	@Secured({"ROLE_DRIVER"})
	@RequestMapping(value = "/collectTicket", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> collectTicket(@RequestBody CollectTicketWrapper collectTicket, @PathVariable String tenantid)
	{
		CustomResponseWrapper response = new CustomResponseWrapper();
		LinesLogic ll = new LinesLogic(tenantid);
		
		try 
		{
			ll.CollectTicket(collectTicket.travelId, collectTicket.ticketNumber);
			response.setSuccess(true);
			response.setMsg("El boleto ha sido cobrado");
		}
		catch (CollectTicketException e)
		{
			response.setSuccess(false);
			response.setMsg(e.getMessage());
			System.out.println(e.getMessage());
		}
		
		return new ResponseEntity<CustomResponseWrapper>(response, HttpStatus.OK);
	}

	@Secured({"ROLE_CLIENT", "ROLE_SALES"})
	@RequestMapping(value = "/buyTicket", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<Void> buyTicket(@RequestBody BuyTicketWrapper buyTicket, @PathVariable String tenantid, HttpServletRequest request)
	{
		Usuario currentUser = context.GetUser(request, tenantid);		
		LinesLogic ll = new LinesLogic(tenantid);
		if (buyTicket.rUser != null)
		{
			UsersLogic ul = new UsersLogic(tenantid);
			Usuario comprador = ul.GetUserByName(buyTicket.rUser);
			ll.buyTickets(comprador, currentUser, buyTicket.id_viaje, buyTicket.origen, buyTicket.destino, buyTicket.valor, buyTicket.seleccionados);
		}
		else if (buyTicket.rDoc != null)
		{
			ll.buyTickets(buyTicket.rDoc, currentUser, buyTicket.id_viaje, buyTicket.origen, buyTicket.destino, buyTicket.valor, buyTicket.seleccionados);
		}
		else
		{
			ll.buyTickets(currentUser, buyTicket.id_viaje, buyTicket.origen, buyTicket.destino, buyTicket.valor, buyTicket.seleccionados);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
