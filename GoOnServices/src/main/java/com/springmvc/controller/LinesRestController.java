package com.springmvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.entities.tenant.ViajeUbicacion;
import com.springmvc.enums.DayOfWeek;
import com.springmvc.exceptions.BusInServiceException;
import com.springmvc.exceptions.BusTravelConcurrencyException;
import com.springmvc.exceptions.BusyDriverException;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;
import com.springmvc.requestWrappers.LinesWrapper;
import com.springmvc.requestWrappers.TravelFormWrapper;
import com.springmvc.requestWrappers.TravelLocationTravel;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class LinesRestController{
	
	@Autowired
    private UserContext context;
	
	@Secured({"ROLE_ADMIN", "ROLE_COORDINATOR"})
	@RequestMapping(value = "/getLines", method = RequestMethod.GET)
    public ResponseEntity<List<Linea>> getLines(@PathVariable String tenantid)
    {
		List<Linea> lineas = new LinesLogic(tenantid).getLineas();
		return new ResponseEntity<List<Linea>>(lineas, HttpStatus.OK);
    }
	
	@Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/createLine", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<Void> CreateLine(@RequestBody LinesWrapper linesWrapper, @PathVariable String tenantid) throws Exception
	{
		LinesLogic tl = new LinesLogic(tenantid);
		
		
		Linea linea = new Linea();		
		
		Parada parada_origen = tl.findParadaByID(linesWrapper.getParadas().get(0).getId_parada()); 
		linea.setOrigen(parada_origen);
		
		Parada parada_destino = tl.findParadaByID(linesWrapper.getParadas().get(linesWrapper.getParadas().size()-1).getId_parada()); 
		linea.setDestino(parada_destino);
		
		linea.setNumero(linesWrapper.getNumero());
		linea.setCosto_minimo(linesWrapper.getCosto_minimo());
		linea.setCosto_maximo(linesWrapper.getCosto_maximo());
		linea.setTiempo_estimado(linesWrapper.getTiempo_estimado());
		linea.setViaja_parado(linesWrapper.getViaja_parado());	
		linea.setHabilitado(true);
		for (int i = 0; i < linesWrapper.getParadas().size(); i++) 
		{
			if (linesWrapper.getParadas().get(i).getId_parada() > 0)
			{
				Parada auxParada = tl.findParadaByID(linesWrapper.getParadas().get(i).getId_parada());
				linea.getParadas().add(auxParada);
			}
			else
			{
				Parada auxParada = new Parada();
				auxParada.setId_parada(linesWrapper.getParadas().get(i).getId_parada());
				auxParada.setDescripcion(linesWrapper.getParadas().get(i).getDescripcion());
				auxParada.setDireccion(linesWrapper.getParadas().get(i).getDireccion());
				auxParada.setEs_peaje(linesWrapper.getParadas().get(i).getEs_peaje());
				auxParada.setEs_terminal(linesWrapper.getParadas().get(i).getEs_terminal());
				auxParada.setLatitud(linesWrapper.getParadas().get(i).getLatitud());
				auxParada.setLongitud(linesWrapper.getParadas().get(i).getLongitud());
				auxParada.setReajuste(linesWrapper.getParadas().get(i).getReajuste());
				linea.getParadas().add(auxParada);
			}
		}		
		
		tl.insertLine(linea);	
		
		/*Insercion de vuelta*/
		if (linesWrapper.getGeneraVuelta())
		{
			Linea vuelta = new Linea();	
			
			Parada parada_origen_vuelta = tl.findParadaByID(linesWrapper.getParadas().get(linesWrapper.getParadas().size()-1).getId_parada());			 
			vuelta.setOrigen(parada_origen_vuelta);
			
			Parada parada_destino_vuelta = tl.findParadaByID(linesWrapper.getParadas().get(0).getId_parada());			
			vuelta.setDestino(parada_destino_vuelta);
			
			
			vuelta.setNumero(linesWrapper.getNumero());
			vuelta.setCosto_minimo(linesWrapper.getCosto_minimo());
			vuelta.setCosto_maximo(linesWrapper.getCosto_maximo());
			
			vuelta.setTiempo_estimado(linesWrapper.getTiempo_estimado_vuelta());
			vuelta.setViaja_parado(linesWrapper.getViaja_parado());	
			vuelta.setHabilitado(true);
			
			for (int i = 0; i < linesWrapper.getParadasV().size(); i++) 
			{
				if (linesWrapper.getParadasV().get(i).getId_parada() > 0)
				{
					long auxID = linesWrapper.getParadasV().get(i).getId_parada();
					Parada auxParadaV = tl.findParadaByID(auxID);
					
					vuelta.getParadas().add(auxParadaV);
				}
				else
				{
					Parada auxParada = new Parada();
					auxParada.setId_parada(linesWrapper.getParadasV().get(i).getId_parada());
					auxParada.setDescripcion(linesWrapper.getParadasV().get(i).getDescripcion());
					auxParada.setDireccion(linesWrapper.getParadasV().get(i).getDireccion());
					auxParada.setEs_peaje(linesWrapper.getParadasV().get(i).getEs_peaje());
					auxParada.setEs_terminal(linesWrapper.getParadasV().get(i).getEs_terminal());
					auxParada.setLatitud(linesWrapper.getParadasV().get(i).getLatitud());
					auxParada.setLongitud(linesWrapper.getParadasV().get(i).getLongitud());
					auxParada.setReajuste(linesWrapper.getParadasV().get(i).getReajuste());
					vuelta.getParadas().add(auxParada);
				}
			}
			tl.insertLine(vuelta);	
		}
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/deleteLine", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> DeleteLine(@RequestBody long id_linea, @PathVariable String tenantid)
	{
		LinesLogic tl = new LinesLogic(tenantid);
		CustomResponseWrapper respuesta = new CustomResponseWrapper();
		if (tl.TieneViajes(id_linea))
		{
			respuesta.setMsg("No se puede eliminar vehiculo, tiene viajes asociados.");
			respuesta.setSuccess(false);	
		}	
		else
		{
			tl.deleteLinea(id_linea);
			respuesta.setSuccess(true);
		}
		return new ResponseEntity<CustomResponseWrapper>(respuesta, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/lineExists", method = RequestMethod.GET)
    public ResponseEntity<Void> lineExists(@PathVariable String tenantid, @RequestParam(value="linenumber") long linenumber) throws AuthenticationException 
    {
		//deberia tirar true si hay 2 o mas lineas con ese numero
    	boolean lineExists = new LinesLogic(tenantid).lineExists(linenumber);
    	if(lineExists)
    	{
    		return new ResponseEntity<Void>(HttpStatus.OK);   		
    	}
    	else
    	{
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	}
    }
	
	@Secured({"ROLE_DRIVER"})
	@RequestMapping(value = "/getLastTravelByDriver", method = RequestMethod.GET)
    public ResponseEntity<Viaje> GetLastTravelByDriver(@PathVariable String tenantid, HttpServletRequest request) 
    {
		LinesLogic ll = new LinesLogic(tenantid);
		Usuario driver = context.GetUser(request, tenantid);
		Viaje travel = null;
		if (driver != null)
		{
			travel = ll.GetLastTravelByDriver(driver);
		}
		return new ResponseEntity<Viaje>(travel, HttpStatus.OK);
    }
	
	@Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/createTravel", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> createTravel(@RequestBody TravelFormWrapper travel, @PathVariable String tenantid)
	{
		CustomResponseWrapper response = new CustomResponseWrapper();
		
		travel.dayFrom.setTimeZone(TimeZone.getDefault());
		travel.dayTo.setTimeZone(TimeZone.getDefault());
		
		LinesLogic tl = new LinesLogic(tenantid);
		Viaje travelToPersist = new Viaje();
		travelToPersist.setConductor(travel.driver);
		travelToPersist.setLinea(travel.line);
		travelToPersist.setVehiculo(travel.bus);
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
	    Date time = null;
	    try {
	    	time = sdf.parse(travel.time);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    
		Map<DayOfWeek, Boolean> days = new  HashMap<DayOfWeek, Boolean>();
		days.put(DayOfWeek.Monday, travel.monday);
		days.put(DayOfWeek.Tuesday, travel.tuesday);
		days.put(DayOfWeek.Wednesday, travel.wednesday);
		days.put(DayOfWeek.Thursday, travel.thursday);
		days.put(DayOfWeek.Friday, travel.friday);
		days.put(DayOfWeek.Saturday, travel.saturday);
		days.put(DayOfWeek.Sunday, travel.sunday);
		
		try 
		{
			int createdTravels = tl.CreateTravels(travelToPersist, days, travel.dayFrom, travel.dayTo, time);
			if(createdTravels > 0)
			{
				response.setSuccess(true);
				response.setMsg(String.format("Cantidad de viajes creados: %s", createdTravels));
			}
			else
			{
				response.setSuccess(false);
				response.setMsg("No se ha generado ningún viaje. Valide el rango de fechas y la periodicidad.");
			}
		}
		catch (BusInServiceException e) 
		{
			response.setSuccess(false);
			response.setMsg(e.getMessage());
		}
		catch (BusTravelConcurrencyException e) 
		{
			response.setSuccess(false);
			response.setMsg(e.getMessage());
		}
		catch (BusyDriverException e) 
		{
			response.setSuccess(false);
			response.setMsg(e.getMessage());
		}
		
		return new ResponseEntity<CustomResponseWrapper>(response, HttpStatus.OK);
	}
	
	@Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/getTravels", method = RequestMethod.GET)
	public ResponseEntity<List<Viaje>> getTravels(@RequestParam Date filterDate ,@PathVariable String tenantid)
	{
		List<Viaje> travels = new LinesLogic(tenantid).GetTravels(filterDate);
		return new ResponseEntity<List<Viaje>>(travels, HttpStatus.OK);
	}
	
	@Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/deleteTravel", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> deleteTravel(@RequestBody long travelId, @PathVariable String tenantid)
	{
		LinesLogic tl = new LinesLogic(tenantid);
		CustomResponseWrapper respuesta = new CustomResponseWrapper();
		if (tl.IsTravelInUse(travelId))
		{
			respuesta.setMsg("No se puede eliminar el viaje, ya tiene gestionados viajes y/o encomiendas.");
			respuesta.setSuccess(false);	
		}	
		else
		{
			tl.deleteTravel(travelId);
			respuesta.setSuccess(true);
		}
		return new ResponseEntity<CustomResponseWrapper>(respuesta, HttpStatus.OK);
	}
	
	@Secured({"ROLE_DRIVER"})
	@RequestMapping(value = "/startTravel", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Void> startTravel(@RequestParam long travelId, @PathVariable String tenantid)
	{
		LinesLogic ll = new LinesLogic(tenantid);
		ll.StartTravel(travelId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	
	
	@Secured({"ROLE_DRIVER"})
	@RequestMapping(value = "/finishTravel", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Void> finishTravel(@RequestParam long travelId, @PathVariable String tenantid)
	{
		LinesLogic ll = new LinesLogic(tenantid);
		ll.FinishTravel(travelId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@Secured({"ROLE_CLIENT"})
	@RequestMapping(value = "/getLastTravelLocation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ViajeUbicacion> GetLastTravelLocation(@RequestParam long travelId, @PathVariable String tenantid)
	{
		LinesLogic ll = new LinesLogic(tenantid);
		ViajeUbicacion travelLoc = ll.GetLastTravelLocation(travelId);
		return new ResponseEntity<ViajeUbicacion>(travelLoc, HttpStatus.OK);
	}
	
	@Secured({"ROLE_DRIVER"})
	@RequestMapping(value = "/setTravelLocation", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<Void> setTravelLocation(@RequestBody TravelLocationTravel travelLoc, @PathVariable String tenantid)
	{
		LinesLogic ll = new LinesLogic(tenantid);
		ll.InsertTravelLocation(travelLoc.travelId, travelLoc.lat, travelLoc.lng);
		return new ResponseEntity(HttpStatus.OK);
	}
}
