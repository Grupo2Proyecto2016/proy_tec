package com.springmvc.dataaccess.repository.main;


import org.springframework.data.repository.CrudRepository;

import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.main.Usuario;

public interface EmpresaRepository extends CrudRepository<Empresa, Long>
{
	Empresa findBynombretenant(String nombretenant);
}
