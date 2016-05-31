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


import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.logic.implementations.BranchesLogic;
import com.springmvc.logic.implementations.VehiculosLogic;
import com.springmvc.requestWrappers.CustomResponseWrapper;

@RestController
@RequestMapping(value = "/{tenantid}")
public class BranchesRestController {
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createBranch", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateBranch(@RequestBody Sucursal sucursal, @PathVariable String tenantid)
    {
		BranchesLogic bl = new BranchesLogic(tenantid);	    	
		bl.createBranch(sucursal);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }
	
	@RequestMapping(value = "/getBranches", method = RequestMethod.GET)
    public ResponseEntity<List<Sucursal>> GetBranches(@PathVariable String tenantid)
    {
		BranchesLogic bl = new BranchesLogic(tenantid);	    	
		List<Sucursal> branches = bl.GetBranches();		
		return new ResponseEntity<List<Sucursal>>(branches, HttpStatus.OK);		
    }
	
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<CustomResponseWrapper> DeleteBranch(@RequestBody long id_sucursal, @PathVariable String tenantid)
	{
		BranchesLogic bl = new BranchesLogic(tenantid);
		CustomResponseWrapper respuesta = new CustomResponseWrapper();
		if (bl.TieneEmpleados(id_sucursal))
		{
			respuesta.setMsg("No se puede eliminar la sucursal, tiene empleados asociados.");
			respuesta.setSuccess(false);
		}
		else
		{
			bl.deleteBranch(id_sucursal);
			respuesta.setSuccess(true);
		}
		return new ResponseEntity<CustomResponseWrapper>(respuesta, HttpStatus.OK);
	}	
}
