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

import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.logic.implementations.MantenimientoLogic;
import com.springmvc.logic.interfaces.IMantenimientoLogic;

@RestController
@RequestMapping(value = "/{tenantid}")
public class MantenimientoRestController {
	
    private IMantenimientoLogic mantenimientoLogic;

    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getMantenimiento", method = RequestMethod.GET)
    public ResponseEntity<List<Mantenimiento>>  getMantenimiento(@PathVariable String tenantid)
    {
    	List<Mantenimiento> mantenimientos = new MantenimientoLogic(tenantid).GetMantenimientos();
    	return new ResponseEntity<List<Mantenimiento>>(mantenimientos, HttpStatus.OK);
    }
	
    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createMantenimiento", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateMantenimiento(@RequestBody Mantenimiento mantenimiento, @PathVariable String tenantid)
    {
    	MantenimientoLogic ml = new MantenimientoLogic(tenantid);		
		ml.createMantenimiento(mantenimiento);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	
}
