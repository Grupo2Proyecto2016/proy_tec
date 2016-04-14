package com.dataAccess;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EmpresaRepository extends CrudRepository<Empresa, Long>{
	List<Empresa> findByNombre(String nombre);
}
