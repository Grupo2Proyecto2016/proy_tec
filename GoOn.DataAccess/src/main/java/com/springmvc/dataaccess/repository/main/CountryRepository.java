package com.springmvc.dataaccess.repository.main;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springmvc.entities.main.Pais;

@Repository
public interface CountryRepository extends CrudRepository<Pais, Long>{

}
