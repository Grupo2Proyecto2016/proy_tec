package com.springmvc.controller;

import java.util.List;

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
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.requestWrappers.LinesWrapper;

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
		linea.setCosto_fijo(linesWrapper.getCosto_fijo());
		linea.setTiempo_estimado(linesWrapper.getTiempo_estimado());
		linea.setViaja_parado(linesWrapper.getViaja_parado());	
		
		for (int i = 0; i < linesWrapper.getParadas().size()-1; i++) 
		{
			Parada auxParada = new Parada();
			auxParada.setDescripcion(linesWrapper.getParadas().get(i).getDescripcion());
			auxParada.setDireccion(linesWrapper.getParadas().get(i).getDireccion());
			auxParada.setEs_peaje(linesWrapper.getParadas().get(i).getEs_peaje());
			auxParada.setEs_terminal(linesWrapper.getParadas().get(i).getEs_terminal());
			auxParada.setLatitud(linesWrapper.getParadas().get(i).getLatitud());
			auxParada.setLongitud(linesWrapper.getParadas().get(i).getLongitud());
			auxParada.setReajuste(linesWrapper.getParadas().get(i).getReajuste());
			linea.getParadas().add(auxParada);
		}		
		
		tl.insertLine(linea);	
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
}
