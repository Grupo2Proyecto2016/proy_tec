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
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.logic.interfaces.IVehiculosLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;

@RestController
@RequestMapping(value = "/{tenantid}")
public class BusesRestController {
	
    private IVehiculosLogic vehiculosLogic;

    @Secured({"ROLE_ADMIN", "ROLE_COORDINATOR"})
	@RequestMapping(value = "/getBuses", method = RequestMethod.GET)
    public ResponseEntity<List<Vehiculo>>  getBuses(@PathVariable String tenantid)
    {
    	List<Vehiculo> buses = new VehiculosLogic(tenantid).GetVehiculos();
    	return new ResponseEntity<List<Vehiculo>>(buses, HttpStatus.OK);
    }
	
    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createBus", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateBus(@RequestBody Vehiculo vehiculo, @PathVariable String tenantid)
    {
    	int number = 0;
		VehiculosLogic vl = new VehiculosLogic(tenantid);
		
		for (int i = 0; i < vehiculo.getCantAccesibles(); i++) 
		{
	    	number++;
			Asiento auxAsiento = new Asiento();
			auxAsiento.setId_asiento(0);
			auxAsiento.setNumero(number);
			auxAsiento.setEs_ventana(true);
			auxAsiento.setHabilitado(true);
			auxAsiento.setEs_accesible(true);
			vehiculo.getAsientos().add(auxAsiento);
		}
		
		for (int i = 0; i < vehiculo.getCantAsientos(); i++) 
		{
			number++;
	    	boolean isWindow = number % 4 == 0 || number % 4 == 1;
			Asiento auxAsiento = new Asiento();
			auxAsiento.setId_asiento(0);
			auxAsiento.setNumero(number);
			auxAsiento.setEs_ventana(isWindow);
			auxAsiento.setHabilitado(true);	
			auxAsiento.setEs_accesible(false);
			vehiculo.getAsientos().add(auxAsiento);			
		}		
		vl.createBus(vehiculo);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	
    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/deleteBus", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<CustomResponseWrapper> DeleteBus(@RequestBody long id_vehiculo, @PathVariable String tenantid)
    {
		VehiculosLogic vl = new VehiculosLogic(tenantid);
		CustomResponseWrapper respuesta = new CustomResponseWrapper();
		if (vl.TieneViajes(id_vehiculo))
		{
			respuesta.setMsg("No se puede eliminar vehiculo, tiene viajes asociados.");
			respuesta.setSuccess(false);	
		}	
		else
		{
			vl.deleteBus(id_vehiculo);
			respuesta.setSuccess(true);
		}
		return new ResponseEntity<CustomResponseWrapper>(respuesta, HttpStatus.OK);
    }
}
