package com.springmvc.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.tenant.Parametro;
import com.springmvc.logic.implementations.ParametersLogic;
import com.springmvc.logic.implementations.TenantLogic;
import com.springmvc.logic.interfaces.ITenantLogic;

@RestController
@RequestMapping(value = "/{tenantid}")
public class CompanyRestController 
{
	@Autowired 
    private ITenantLogic tenantLogic;
	
	@RequestMapping(value = "/updateCompany", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> UpdateCompany(@RequestBody Empresa company, @PathVariable String tenantid)
    {
		if(company.logo != null)
		{
			//save file
			company.setLogocontent(company.logo.getBytes(Charset.forName("UTF-8")));
		}
		tenantLogic.UpdateCompany(tenantid, company);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/getParams", method = RequestMethod.GET)
    public ResponseEntity<List<Parametro>> GetParameters(@PathVariable String tenantid)
    {
		List<Parametro> parameters = new ParametersLogic(tenantid).GetAll();
    	return new ResponseEntity<List<Parametro>>(parameters, HttpStatus.OK);
    }
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/updateParameters", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> UpdateCompany(@RequestBody List<Parametro> parameters, @PathVariable String tenantid)
    {
		new ParametersLogic(tenantid).UpdateParameters(parameters);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
