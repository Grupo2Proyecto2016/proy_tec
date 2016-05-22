package com.springmvc.controller;
 
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.logic.implementations.UsersLogic;

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
}