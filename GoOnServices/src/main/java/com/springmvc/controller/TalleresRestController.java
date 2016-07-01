package com.springmvc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.entities.tenant.Asiento;
import com.springmvc.entities.tenant.Taller;
import com.springmvc.logic.implementations.TallerLogic;
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.logic.interfaces.ITallerLogic;
import com.springmvc.logic.interfaces.IVehiculosLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;

@RestController
@RequestMapping(value = "/{tenantid}")
public class TalleresRestController {
	
    private ITallerLogic tallerLogic;

    @Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/getTalleres", method = RequestMethod.GET)
    public ResponseEntity<List<Taller>>  getTaller(@PathVariable String tenantid)
    {
    	List<Taller> talleres = new TallerLogic(tenantid).getTalleres();
    	return new ResponseEntity<List<Taller>>(talleres, HttpStatus.OK);
    }
	
    @Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/createTaller", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateTaller(@RequestBody Taller taller, @PathVariable String tenantid)
    {
		TallerLogic tl = new TallerLogic(tenantid);
		tl.createTaller(taller);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	/*
    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/deleteBus", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<CustomResponseWrapper> DeleteBus(@RequestBody long id_vehiculo, @PathVariable String tenantid)
    {
		VehiculosLogic vl = new VehiculosLogic(tenantid);
		CustomResponseWrapper respuesta = new CustomResponseWrapper();
		
			vl.deleteBus(id_vehiculo);
			respuesta.setSuccess(true);
		
		return new ResponseEntity<CustomResponseWrapper>(respuesta, HttpStatus.OK);
    }*/
}
