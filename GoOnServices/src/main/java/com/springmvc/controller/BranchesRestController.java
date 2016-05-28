package com.springmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.logic.implementations.BranchesLogic;

@RestController
@RequestMapping(value = "/{tenantid}")
public class BranchesRestController {
	
	
	@RequestMapping(value = "/createBranch", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateBus(@RequestBody Sucursal sucursal, @PathVariable String tenantid)
    {
		BranchesLogic bl = new BranchesLogic(tenantid);	    	
		bl.createBranch(sucursal);		
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
    }

}
