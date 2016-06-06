package com.springmvc.controller;

import java.awt.Dimension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.enums.Parameter;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.logic.implementations.ParametersLogic;
import com.springmvc.requestWrappers.CalcWrapper;
import com.springmvc.requestWrappers.LinesWrapper;


@RestController
@RequestMapping(value = "/{tenantid}")
public class PackageRestController{
	
	@RequestMapping(value = "/calcPackage", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<String> CalcPackage(@RequestBody CalcWrapper calcWrapper, @PathVariable String tenantid) throws Exception
	{
		ParametersLogic pl = new ParametersLogic(tenantid);
		float priceByKm = pl.FindById(Parameter.PriceByPackageKm.getValue()).getValor();
		float priceByKg = pl.FindById(Parameter.PriceByKg.getValue()).getValor();
		float priceByVolume = pl.FindById(Parameter.PriceByVolume.getValue()).getValor();
		
		float volumePrice = calcWrapper.volume * priceByVolume;
		float kgPrice = calcWrapper.weigth * priceByKg;
		float distancePrice = calcWrapper.distance * priceByKm;
		int finalPrice = 0;
		
		if(volumePrice > kgPrice)
		{
			finalPrice = (int) (distancePrice * volumePrice);
		}
		else
		{
			finalPrice = (int) (distancePrice * kgPrice);
		}

		return new ResponseEntity<String>(Integer.toString(finalPrice), HttpStatus.CREATED);
	}
}
