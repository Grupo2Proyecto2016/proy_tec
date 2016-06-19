package com.springmvc.logic.implementations;

import java.util.List;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Parametro;
import com.springmvc.enums.Parameter;
import com.springmvc.logic.interfaces.IParametersLogic;

public class ParametersLogic implements IParametersLogic 
{	
	private TenantDAContext TenantContext;
	
	public ParametersLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	public Parametro FindById(int id)
	{
		return TenantContext.ParametroRepository.FindById(id);
	}
	
	public void InsertParameter(Parametro parameter)
	{
		TenantContext.ParametroRepository.InsertParameter(parameter);
	}

	public List<Parametro> GetAll()
	{
		return TenantContext.ParametroRepository.GetAll();
	}

	public void UpdateParameters(List<Parametro> parameters)
	{
		for (Parametro parameter : parameters) 
		{
			TenantContext.ParametroRepository.UpdateParameter(parameter);
		}
	}
	
	public void SetUpParameters() 
	{
		Parametro priceByKg = new Parametro();
		priceByKg.setId(Parameter.PriceByKg.getValue());
		priceByKg.setNombre("PriceByKg");
		priceByKg.setValor(1);
		InsertParameter(priceByKg);
		
		Parametro priceByVolume = new Parametro();
		priceByVolume.setId(Parameter.PriceByVolume.getValue());
		priceByVolume.setNombre("PriceByVolume");
		priceByVolume.setValor(1);
		InsertParameter(priceByVolume);
		
		Parametro priceByTravelKm = new Parametro();
		priceByTravelKm.setId(Parameter.PriceByTravelKm.getValue());
		priceByTravelKm.setNombre("PriceByTravelKm");
		priceByTravelKm.setValor(1);
		InsertParameter(priceByTravelKm);
		
		Parametro priceByPackageKm = new Parametro();
		priceByPackageKm.setId(Parameter.PriceByPackageKm.getValue());
		priceByPackageKm.setNombre("PriceByPackageKm");
		priceByPackageKm.setValor(1);
		InsertParameter(priceByPackageKm);
		
		Parametro maxReservationDelay = new Parametro();
		maxReservationDelay.setId(Parameter.MaxReservationDelay.getValue());
		maxReservationDelay.setNombre("MaxReservationDelay");
		maxReservationDelay.setValor(1);
		InsertParameter(maxReservationDelay);
		
		Parametro stopDelay = new Parametro();
		stopDelay.setId(Parameter.StopDelay.getValue());
		stopDelay.setNombre("StopDelay");
		stopDelay.setValor(1);
		InsertParameter(stopDelay);
		
		Parametro packageBasePrice = new Parametro();
		packageBasePrice.setId(Parameter.PackageBasePrice.getValue());
		packageBasePrice.setNombre("packageBasePrice");
		packageBasePrice.setValor(20);
		InsertParameter(packageBasePrice);
	}
}
