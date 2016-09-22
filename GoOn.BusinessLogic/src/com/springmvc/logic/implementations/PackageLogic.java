package com.springmvc.logic.implementations;

import java.util.Date;
import java.util.List;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Encomienda;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Parametro;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.entities.tenant.Viaje;
import com.springmvc.enums.PackageStatus;
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
		Viaje travel = TenantContext.ViajeRepository.FindByID(travelId);
		
		Encomienda pack = new Encomienda();
		pack.setStatus(PackageStatus.Created.getValue());
		pack.setViaje(travel);
		pack.setPrecio(price);
		pack.setCi_emisor(sender.getCi());
		pack.setCi_receptor(receipt.getCi());
		pack.setPeso(weigth);
		pack.setVolumen(volume);
		pack.setUsr_crea(employee);
		if(sender.getIdUsuario() != 0)
		{
			pack.setUsr_envia(sender);
		}
		if(receipt.getIdUsuario() != 0)
		{
			pack.setUsr_recibe(receipt);
		}
		
		TenantContext.EncomiendaRepository.AddPackage(pack);
	}

	public List<Encomienda> GetUserPackages(Usuario currentUser) 
	{
		return TenantContext.EncomiendaRepository.GetByClient(currentUser.getIdUsuario());
	}

	public List<Encomienda> GetBranchPackages(Parada terminal, Date from, Date to) 
	{
		List<Encomienda> packages = TenantContext.EncomiendaRepository.GetBranchPackages(terminal, from, to);
		for (Encomienda pack : packages) 
		{
			pack.ShowDeliverButton = pack.getViaje().getLinea().getDestino().getId_parada() == terminal.getId_parada() 
					&& pack.getStatus() == PackageStatus.Transported.getValue()
			;
		}
		return packages;
	}

	public void DeliverPackage(long id_encomienda) 
	{
		TenantContext.EncomiendaRepository.DeliverPackage(id_encomienda);
	}
}
