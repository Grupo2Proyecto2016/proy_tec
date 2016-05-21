package com.springmvc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.logic.interfaces.IVehiculosLogic;

@RestController
@RequestMapping(value = "/{tenantid}")
public class BusesRestController {
	
    private IVehiculosLogic vehiculosLogic;

	@RequestMapping(value = "/getBuses", method = RequestMethod.GET)
    public ResponseEntity<List<Vehiculo>>  getBuses(@PathVariable String tenantid)
    {
    	List<Vehiculo> buses = new VehiculosLogic(tenantid).GetVehiculos();
    	return new ResponseEntity<List<Vehiculo>>(buses, HttpStatus.OK);
    }
	
}
