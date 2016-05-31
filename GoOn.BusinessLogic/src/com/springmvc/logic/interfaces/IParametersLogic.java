package com.springmvc.logic.interfaces;

import java.util.List;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.tenant.Parametro;
import com.springmvc.enums.Parameter;

public interface IParametersLogic {
	
	Parametro FindById(int id);
	
	void InsertParameter(Parametro parameter);
	
	List<Parametro> GetAll();
	
	void UpdateParameters(List<Parametro> parameters);
	
	void SetUpParameters(); 
}
