package com.springmvc.logic.interfaces;

import java.util.List;

import com.springmvc.entities.tenant.Taller;

public interface ITallerLogic {

	public void createTaller(Taller taller);
	public List<Taller> getTalleres();
}
