package com.springmvc.controller;
 
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.main.Pais;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.enums.UserRol;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.requestWrappers.CompanyWrapper;
import com.springmvc.requestWrappers.UserWrapper;

@RestController
@RequestMapping(value = "/{tenantid}")
public class UserRestController 
{
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>>  getUsers(@PathVariable String tenantid, HttpServletRequest request)
    {
    	List<Usuario> users = new UsersLogic(tenantid).GetEmployees();
    	return new ResponseEntity<List<Usuario>>(users, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/userExists", method = RequestMethod.GET)
    public ResponseEntity<Void> userExists(@PathVariable String tenantid, @RequestParam(value="username") String username) throws AuthenticationException 
    {
    	boolean userExists = new UsersLogic(tenantid).GetUserByName(username) != null;
    	if(userExists)
    	{
    		return new ResponseEntity<Void>(HttpStatus.OK);   		
    	}
    	else
    	{
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	}
    }
	
	@RequestMapping(value = "/createUser", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateUser(@RequestBody UserWrapper user, @PathVariable String tenantid)
    {
		Usuario userToPersist = new Usuario();
    	String hashedPass = new BCryptPasswordEncoder().encode(user.passwd);
    	userToPersist.setPasswd(hashedPass);
    	userToPersist.setNombre(user.nombre);
    	userToPersist.setApellido(user.apellido);
    	userToPersist.setUsrname(user.usrname);
    	userToPersist.setEmail(user.email);
    	userToPersist.setTelefono(user.telefono);
    	userToPersist.setDireccion(user.direccion);
    	userToPersist.setUltimoResetPassword(new Date());
    	userToPersist.setRol_id_rol(user.rol_id_rol);
    	userToPersist.setFch_nacimiento(user.fch_nacimiento);
    	userToPersist.setEnabled(true);
    	userToPersist.setEs_empleado(true);
    	
    	new UsersLogic(tenantid).CreateUser(userToPersist);
    	
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> UpdateUser(@RequestBody UserWrapper user, @PathVariable String tenantid)
    {
		Usuario userUpdateData = new Usuario();
		userUpdateData.setUsrname(user.usrname);
		userUpdateData.setNombre(user.nombre);
		userUpdateData.setApellido(user.apellido);
		userUpdateData.setEmail(user.email);
		userUpdateData.setTelefono(user.telefono);
		userUpdateData.setDireccion(user.direccion);
		userUpdateData.setRol_id_rol(user.rol_id_rol);
		userUpdateData.setFch_nacimiento(user.fch_nacimiento);
    	
    	new UsersLogic(tenantid).UpdateUser(userUpdateData);
    	
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> DeleteUser(@RequestBody UserWrapper username, @PathVariable String tenantid)
    {
    	new UsersLogic(tenantid).DeleteUser(username.usrname);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
}