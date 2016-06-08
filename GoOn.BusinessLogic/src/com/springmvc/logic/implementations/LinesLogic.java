package com.springmvc.logic.implementations;

import java.util.List;

import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Linea;
import com.springmvc.entities.tenant.Parada;
import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.logic.interfaces.ILinesLogic;

public class LinesLogic implements ILinesLogic
{
	
	private TenantDAContext TenantContext;
	
	public LinesLogic(String tenant)
	{
		TenantContext = new TenantDAContext(tenant, false);
	}

	public void createTerminal(Parada terminal) 
	{
		terminal.setEs_terminal(true);
		TenantContext.ParadaRepository.InsertStation(terminal);		
	}

	public List<Parada> GetTerminals() 
	{
		return TenantContext.ParadaRepository.GetTerminals();	
	}
	
	public void insertLine(Linea linea)
	{
		TenantContext.LineaRepository.InsertLine(linea);
	}
	
	public Linea findByID(long id_linea)
	{
		return TenantContext.LineaRepository.findByID(id_linea);
	}

	public Parada findParadaByID(long id_parada) 
	{
		return TenantContext.ParadaRepository.findByID(id_parada);
	}

	public List<Linea> getLineas() 
	{
		return TenantContext.LineaRepository.getLineas();
	}
}
