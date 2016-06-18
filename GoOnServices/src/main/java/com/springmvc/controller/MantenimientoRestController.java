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

import com.springmvc.entities.tenant.Mantenimiento;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.logic.implementations.MantenimientoLogic;
import com.springmvc.logic.interfaces.IMantenimientoLogic;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class MantenimientoRestController {
	
	@Autowired
    private UserContext context;
	
    private IMantenimientoLogic mantenimientoLogic;

    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getMantenimientos", method = RequestMethod.GET)
    public ResponseEntity<List<Mantenimiento>>  getMantenimiento(@PathVariable String tenantid)
    {
    	List<Mantenimiento> mantenimientos = new MantenimientoLogic(tenantid).getMantenimientos();
    	return new ResponseEntity<List<Mantenimiento>>(mantenimientos, HttpStatus.OK);
    }
	
    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createMantenimiento", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateMantenimiento(@RequestBody Mantenimiento mantenimiento, @PathVariable String tenantid, HttpServletRequest request)
    {
    	Usuario user = context.GetUser(request, tenantid);
    	if(user == null)
    	{
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	else
    	{
    		mantenimiento.setUser_crea(user);
    	}
    	
    	MantenimientoLogic ml = new MantenimientoLogic(tenantid);		
		ml.createMantenimiento(mantenimiento);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	
}
