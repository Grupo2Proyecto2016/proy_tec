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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.enums.DayOfWeek;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;
import com.springmvc.requestWrappers.LinesWrapper;
import com.springmvc.requestWrappers.TravelFormWrapper;

@RestController
@RequestMapping(value = "/{tenantid}")
public class LinesRestController{
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getLines", method = RequestMethod.GET)
    public ResponseEntity<List<Linea>> getLines(@PathVariable String tenantid)
    {
		List<Linea> lineas = new LinesLogic(tenantid).getLineas();
		return new ResponseEntity<List<Linea>>(lineas, HttpStatus.OK);
    }
	
	@Secured({"ROLE_ADMIN"})
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
		for (int i = 0; i < linesWrapper.getParadas().size()-1; i++) 
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
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_ADMIN"})
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
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createTravel", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<?> createTravel(@RequestBody TravelFormWrapper travel, @PathVariable String tenantid)
	{
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
	    
//	    travel.dayFrom.set((GregorianCalendar.HOUR_OF_DAY), date.getHours());
//	    int b = 2;
//	    int a = travel.dayFrom.get((GregorianCalendar.HOUR_OF_DAY));
	    
		Map<DayOfWeek, Boolean> days = new  HashMap<DayOfWeek, Boolean>();
		days.put(DayOfWeek.Monday, travel.monday);
		days.put(DayOfWeek.Tuesday, travel.tuesday);
		days.put(DayOfWeek.Wednesday, travel.wednesday);
		days.put(DayOfWeek.Thursday, travel.thursday);
		days.put(DayOfWeek.Friday, travel.friday);
		days.put(DayOfWeek.Saturday, travel.saturday);
		days.put(DayOfWeek.Sunday, travel.sunday);
		
		tl.CreateTravels(travelToPersist, days, travel.dayFrom, travel.dayTo, time);
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
