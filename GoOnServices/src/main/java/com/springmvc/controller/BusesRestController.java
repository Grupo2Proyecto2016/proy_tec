package com.springmvc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.entities.tenant.Asiento;
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
	
	@RequestMapping(value = "/createBus", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateBus(@RequestBody Vehiculo vehiculo, @PathVariable String tenantid)
    {
		VehiculosLogic vl = new VehiculosLogic(tenantid);
		//List<Asiento> asientos = new ArrayList<>();
		Collection<Asiento> asientos = new ArrayList<Asiento>();
		for (int i = 0; i < vehiculo.getCantAsientos(); i++) 
		{
			Asiento auxAsiento = new Asiento();
			auxAsiento.setNumero(i+1);
			auxAsiento.setEs_ventana(false);
			auxAsiento.setHabilitado(true);
			//auxAsiento.setVehiculo(vehiculo);
			asientos.add(auxAsiento);
			
		}
		vehiculo.setAsientos(asientos);
		vl.createBus(vehiculo);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	
}
