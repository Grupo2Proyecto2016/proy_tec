package com.springmvc.dataaccess.repository.main;


import org.springframework.data.repository.CrudRepository;

import com.springmvc.entities.main.Empresa;

public interface EmpresaRepository extends CrudRepository<Empresa, Long>{
	
}
