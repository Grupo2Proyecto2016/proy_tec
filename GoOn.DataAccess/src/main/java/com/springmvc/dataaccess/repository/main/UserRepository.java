package com.springmvc.dataaccess.repository.main;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springmvc.entities.main.Usuario;

@Repository
public interface UserRepository  extends CrudRepository<Usuario, Long>
{
	Usuario findByUsrname(String usrname);
}
