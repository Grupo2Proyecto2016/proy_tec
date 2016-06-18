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

import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.logic.implementations.BranchesLogic;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;

@RestController
@RequestMapping(value = "/{tenantid}")
public class TerminalsRestController {
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createTerminal", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateBranch(@RequestBody Parada parada, @PathVariable String tenantid)
    {
		LinesLogic tl = new LinesLogic(tenantid);	    	
		tl.createTerminal(parada);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	
	@RequestMapping(value = "/getTerminals", method = RequestMethod.GET)
    public ResponseEntity<List<Parada>> GetTerminals(@PathVariable String tenantid)
    {
		LinesLogic tl = new LinesLogic(tenantid);
		List<Parada> terminals = tl.GetTerminals();		
		return new ResponseEntity<List<Parada>>(terminals, HttpStatus.OK);		
    }
	
	@RequestMapping(value = "/getBranchesTerminals", method = RequestMethod.GET)
    public ResponseEntity<List<Parada>> GetPackageTerminals(@PathVariable String tenantid)
    {
		LinesLogic tl = new LinesLogic(tenantid);
		List<Parada> terminals = tl.GetPackageTerminals();		
		return new ResponseEntity<List<Parada>>(terminals, HttpStatus.OK);		
    }
	
	@RequestMapping(value = "/getPackageOriginTerminals", method = RequestMethod.POST)
    public ResponseEntity<List<Parada>> GetPackageOriginTerminals(@RequestBody Parada parada, @PathVariable String tenantid)
    {
		LinesLogic tl = new LinesLogic(tenantid);
		List<Parada> terminals = tl.GetOriginsFromDestination(parada);
		return new ResponseEntity<List<Parada>>(terminals, HttpStatus.OK);		
    }
	
//	@RequestMapping(value = "/getPackageDestinationTerminals", method = RequestMethod.POST)
//    public ResponseEntity<List<Parada>> GetPackageDestinationTerminals(@RequestBody Parada parada, @PathVariable String tenantid)
//    {
//		LinesLogic tl = new LinesLogic(tenantid);
//		List<Parada> terminals = tl.GetDestinationsFromOrigin(parada);		
//		return new ResponseEntity<List<Parada>>(terminals, HttpStatus.OK);		
//    }
}
