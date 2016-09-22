package com.springmvc.controller;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
import com.springmvc.entities.tenant.Vehiculo;
import com.springmvc.exceptions.BusTravelConcurrencyException;
import com.springmvc.logic.implementations.BranchesLogic;
import com.springmvc.logic.implementations.MantenimientoLogic;
import com.springmvc.logic.interfaces.IMantenimientoLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;
import com.springmvc.requestWrappers.MantenimientoFormWrapper;
import com.springmvc.requestWrappers.TravelFormWrapper;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class MantenimientoRestController {
	
	@Autowired
    private UserContext context;
	
    private IMantenimientoLogic mantenimientoLogic;

    @Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/getMantenimientos", method = RequestMethod.GET)
    public ResponseEntity<List<Mantenimiento>>  getMantenimiento(@PathVariable String tenantid)
    {
    	List<Mantenimiento> mantenimientos = new MantenimientoLogic(tenantid).getMantenimientos();
    	return new ResponseEntity<List<Mantenimiento>>(mantenimientos, HttpStatus.OK);
    }
	
    @Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/findServiceByDate", method = RequestMethod.GET, consumes="application/json", produces = "application/json")
    public ResponseEntity<List<Mantenimiento>> findServiceByDate(@RequestBody TravelFormWrapper viaje, @PathVariable String tenantid)
    {
      	MantenimientoLogic ml = new MantenimientoLogic(tenantid);
      	viaje.dayFrom.setTimeZone(TimeZone.getDefault());
    	viaje.dayTo.setTimeZone(TimeZone.getDefault());
    	List<Mantenimiento> mantenimientos = ml.findServiceByDate(viaje.bus.getId_vehiculo(), viaje.dayFrom, viaje.dayTo);	
    	if(mantenimientos == null)
    	{
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	else
    	{
    		return new ResponseEntity<List<Mantenimiento>>(mantenimientos, HttpStatus.OK);
    	}
    }
    
    @Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/createMantenimiento", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<CustomResponseWrapper> CreateMantenimiento(@RequestBody MantenimientoFormWrapper mantenimiento, @PathVariable String tenantid, HttpServletRequest request)
    {
    	
    	CustomResponseWrapper response = new CustomResponseWrapper();
    	
    	MantenimientoLogic ml = new MantenimientoLogic(tenantid);
    	Mantenimiento mantenimientoToPersist = new Mantenimiento();
    	
    	Usuario user = context.GetUser(request, tenantid);
    	if(user == null)
    	{
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	else
    	{
    		mantenimientoToPersist.setUser_crea(user);
    	}
    	
    	mantenimiento.dayFrom.setTimeZone(TimeZone.getDefault());
    	mantenimiento.dayTo.setTimeZone(TimeZone.getDefault());
    	
    	mantenimientoToPersist.setDescripcion(mantenimiento.getDescripcion());
    	mantenimientoToPersist.setCosto(null);
    	mantenimientoToPersist.setTaller(mantenimiento.taller);
    	mantenimientoToPersist.setVehiculo(mantenimiento.vehiculo);
    	
    	
    	try 
    	{
    		
    		int deli = ml.createMantenimiento(mantenimientoToPersist, mantenimiento.dayFrom, mantenimiento.dayTo);	
    		
    		if (deli == 1)
    		{
    			response.setSuccess(true);
				response.setMsg("Vehiculo enviado al mantenimiento");
			}
//    		else
//    		{
//				response.setSuccess(false);
//				response.setMsg("No se ha generado ningún mantenimiento. Valide el rango de fechas.");
//    		}
    			
    		
		} 
    	catch (BusTravelConcurrencyException e) {
			response.setSuccess(false);
			response.setMsg(e.getMessage());
		}
    	
    	return new ResponseEntity<CustomResponseWrapper>(response, HttpStatus.OK);
				
    }
    
    
    @Secured({"ROLE_COORDINATOR"})
	@RequestMapping(value = "/deleteMantenimiento", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> DeleteBranch(@RequestBody MantenimientoFormWrapper mantenimiento, @PathVariable String tenantid)
	{
		MantenimientoLogic ml = new MantenimientoLogic(tenantid);
		CustomResponseWrapper respuesta = new CustomResponseWrapper();
		
		Mantenimiento mantenimientoSalida = new Mantenimiento();
		mantenimientoSalida.setId_mantenimiento(mantenimiento.getId_mantenimiento());
		mantenimientoSalida.setCosto(mantenimiento.getCosto());
		
		if(mantenimiento.factura != null)
		{
			//save file
			mantenimiento.setFacturaContent(mantenimiento.factura.getBytes(Charset.forName("UTF-8")));
		}
		
		mantenimientoSalida.setFacturaContent(mantenimiento.getFacturaContent());
		
		ml.deleteMantenimiento(mantenimientoSalida);
		respuesta.setSuccess(true);

		return new ResponseEntity<CustomResponseWrapper>(respuesta, HttpStatus.OK);
	}	
	
}
