package com.springmvc.controller;
 
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.springmvc.entities.tenant.Sucursal;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.enums.UserRol;
import com.springmvc.exceptions.UserAlreadyExistsException;
import com.springmvc.logic.implementations.BranchesLogic;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.requestWrappers.CompanyWrapper;
import com.springmvc.requestWrappers.UserWrapper;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class UserRestController 
{
	@Autowired
    private UserContext context;
	
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
    public ResponseEntity<Void> CreateUser(@RequestBody UserWrapper user, @PathVariable String tenantid) throws UserAlreadyExistsException
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
    	if(user.rol_id_rol == UserRol.Sales.getValue() && user.id_sucursal != 0)
    	{
    		BranchesLogic bl = new BranchesLogic(tenantid);
    		Sucursal branch = bl.GetBranch(user.id_sucursal);
    		userToPersist.setSucursal(branch);
    	}
    	else
    	{
    		userToPersist.setSucursal(null);
    	}
    	
    	new UsersLogic(tenantid).CreateUser(userToPersist);
    	
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> RegisterUser(@RequestBody UserWrapper user, @PathVariable String tenantid) throws UserAlreadyExistsException
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
    	userToPersist.setRol_id_rol(UserRol.Client.getValue());
    	userToPersist.setFch_nacimiento(user.fch_nacimiento);
    	userToPersist.setEnabled(true);
    	userToPersist.setEs_empleado(false);
    	
    	new UsersLogic(tenantid).CreateUser(userToPersist);
    	
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> ChangePassword(@RequestBody UserWrapper user, @PathVariable String tenantid, HttpServletRequest request)
    {
		Usuario signedUser = context.GetUser(request);
		String newPassword = new BCryptPasswordEncoder().encode(user.passwd);
    	new UsersLogic(tenantid).UpdateUserPassword(signedUser, newPassword);
    	
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/updateEmployee", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> UpdateEmployee(@RequestBody UserWrapper user, @PathVariable String tenantid)
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
		
		if(user.rol_id_rol == UserRol.Sales.getValue() && user.id_sucursal != 0)
    	{
    		BranchesLogic bl = new BranchesLogic(tenantid);
    		Sucursal branch = bl.GetBranch(user.id_sucursal);
    		userUpdateData.setSucursal(branch);
    	}
    	else
    	{
    		userUpdateData.setSucursal(null);
    	}
    	new UsersLogic(tenantid).UpdateUser(userUpdateData);
    	
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/updateClient", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> UpdateClient(@RequestBody UserWrapper user, @PathVariable String tenantid, HttpServletRequest request)
    {
		String username = new UserContext().GetUsername(request);
		Usuario userUpdateData = new Usuario();
		userUpdateData.setUsrname(username);
		userUpdateData.setNombre(user.nombre);
		userUpdateData.setApellido(user.apellido);
		userUpdateData.setEmail(user.email);
		userUpdateData.setTelefono(user.telefono);
		userUpdateData.setDireccion(user.direccion);
		userUpdateData.setRol_id_rol(UserRol.Client.getValue());
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
	
	@RequestMapping(value = "/deleteSignedUser", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> DeleteSignedUser(@RequestBody String empty, @PathVariable String tenantid, HttpServletRequest request)
    {
		Usuario signedUser = context.GetUser(request);
    	new UsersLogic(tenantid).DeleteUser(signedUser.getUsrname());
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }	
}