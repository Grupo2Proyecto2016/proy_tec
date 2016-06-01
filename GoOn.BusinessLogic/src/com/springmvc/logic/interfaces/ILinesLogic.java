package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.tenant.Parada;

public interface ILinesLogic 
{
	void createTerminal(Parada terminal);
	
	List<Parada> GetTerminals();
}
