package com.springmvc.logic.implementations;

import java.util.List;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Parametro;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.enums.Parameter;
import com.springmvc.logic.interfaces.IParametersLogic;

public class PackageLogic 
{	
	private TenantDAContext TenantContext;
	
	public PackageLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	public int CalcPackage(float distance, float volume, float weigth)
	{
		ParametersLogic pl = new ParametersLogic(TenantContext.TenantName);
		float priceByKm = pl.FindById(Parameter.PriceByPackageKm.getValue()).getValor();
		float priceByKg = pl.FindById(Parameter.PriceByKg.getValue()).getValor();
		float priceByVolume = pl.FindById(Parameter.PriceByVolume.getValue()).getValor();
		float priceBase = pl.FindById(Parameter.PackageBasePrice.getValue()).getValor();

		float volumePrice = volume * priceByVolume;
		float kgPrice = weigth * priceByKg;
		float distancePrice = distance * priceByKm;
		int finalPrice = 0;
		
		finalPrice = (int)Math.max(priceBase, Math.max((distancePrice * volumePrice), (distancePrice * kgPrice)));
		return finalPrice;
	}
	
	public void CreatePackage(float distance, float volume, float weigth, Usuario sender, Usuario receipt, Usuario employee, long travelId)
	{
		int price = CalcPackage(distance, volume, weigth);
	}
}
