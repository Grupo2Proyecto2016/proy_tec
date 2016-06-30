package com.springmvc.controller;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.spi.CalendarDataProvider;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.enums.Parameter;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.PackageLogic;
import com.springmvc.logic.implementations.ParametersLogic;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.requestWrappers.CalcWrapper;
import com.springmvc.requestWrappers.CustomResponseWrapper;
import com.springmvc.requestWrappers.LinesWrapper;
import com.springmvc.requestWrappers.PackageFormWrapper;
import com.springmvc.requestWrappers.UserWrapper;
import com.springmvc.utils.UserContext;


@RestController
@RequestMapping(value = "/{tenantid}")
public class PackageRestController
{
	@Autowired
    private UserContext context;
	
	@RequestMapping(value = "/calcPackage", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<String> CalcPackage(@RequestBody CalcWrapper calcWrapper, @PathVariable String tenantid) throws Exception
	{
		PackageLogic pl = new PackageLogic(tenantid);
		int price = pl.CalcPackage(calcWrapper.distance, calcWrapper.volume, calcWrapper.weigth);

		return new ResponseEntity<String>(Integer.toString(price), HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_CLIENT"})
	@RequestMapping(value = "/userPackages", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Encomienda>> UserPackages(@PathVariable String tenantid, HttpServletRequest request) throws Exception
	{
		Usuario currentUser = context.GetUser(request, tenantid);
		PackageLogic pl = new PackageLogic(tenantid);
		List<Encomienda> packages = pl.GetUserPackages(currentUser);

		return new ResponseEntity<List<Encomienda>>(packages, HttpStatus.OK);
	}
	
	@Secured({"ROLE_SALES"})
	@RequestMapping(value = "/createPackage", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> createPackage(@RequestBody PackageFormWrapper packageWrapper, @PathVariable String tenantid, HttpServletRequest request) throws Exception
	{
		PackageLogic pl = new PackageLogic(tenantid);
		UsersLogic ul = new UsersLogic(tenantid);
		
		Usuario employee = context.GetUser(request, tenantid);
		
		Usuario sender = null;
		if(packageWrapper.eUser != null)
		{
			sender = ul.GetUserByName(packageWrapper.eUser);
		}
		else
		{
			sender = new Usuario();
			sender.setCi(packageWrapper.eDoc);
		}
		
		Usuario receipt = null;
		if(packageWrapper.rUser != null)
		{
			receipt = ul.GetUserByName(packageWrapper.rUser);
		}
		else
		{
			receipt = new Usuario();
			receipt.setCi(packageWrapper.rDoc);
		}
		
		try
		{
			pl.CreatePackage(packageWrapper.distance, packageWrapper.volume, packageWrapper.peso, sender, receipt, employee, packageWrapper.travel_id);		
			CustomResponseWrapper response = new CustomResponseWrapper();
			response.setSuccess(true);
			response.setMsg(null);
			return new ResponseEntity<CustomResponseWrapper>(response, HttpStatus.OK);
		}
		catch(Exception e)
		{
			CustomResponseWrapper response = new CustomResponseWrapper();
			response.setSuccess(false);
			response.setMsg("El viaje seleccionado ya no tiene espacio para encomiendas. Seleccione otro.");
			return new ResponseEntity<CustomResponseWrapper>(response, HttpStatus.OK);
		}
	}
	
	@Secured({"ROLE_SALES"})
	@RequestMapping(value = "/getPackageTravels", method = RequestMethod.POST)
	public ResponseEntity<List<Viaje>> getTravels(@RequestBody Parada destination, @PathVariable String tenantid, HttpServletRequest request)
	{
		Usuario user = context.GetUser(request, tenantid);
		Parada origin = user.getSucursal().getTerminal();
		List<Viaje> travels = new LinesLogic(tenantid).GetPackageTravels(origin, destination);
		return new ResponseEntity<List<Viaje>>(travels, HttpStatus.OK);
	}
	
	@Secured({"ROLE_SALES"})
	@RequestMapping(value = "/getBranchPackages", method = RequestMethod.GET)
	public ResponseEntity<List<Encomienda>> getBranchPackages(@RequestParam Date from, @PathVariable String tenantid, HttpServletRequest request)
	{
		from.setHours(0);
		Calendar to = Calendar.getInstance();
		to.setTime(from);
		to.add(GregorianCalendar.DAY_OF_YEAR, 30);
		
		Usuario user = context.GetUser(request, tenantid);
		Parada terminal = user.getSucursal().getTerminal();
		PackageLogic pl = new PackageLogic(tenantid);
		
		List<Encomienda> packages = pl.GetBranchPackages(terminal, from, to.getTime());

		return new ResponseEntity<List<Encomienda>>(packages, HttpStatus.OK);
	}
	
	@Secured({"ROLE_SALES"})
	@RequestMapping(value = "/deliverPackage", method = RequestMethod.POST)
	public ResponseEntity<Void> deliverPackage(@RequestBody PackageFormWrapper pack, @PathVariable String tenantid, HttpServletRequest request)
	{
		PackageLogic pl = new PackageLogic(tenantid);	
		pl.DeliverPackage(pack.id_encomienda);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}


