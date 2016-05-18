package com.springmvc.controller;
 
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springmvc.entities.main.Usuario;
import com.springmvc.logic.interfaces.IUsersLogic;
import com.springmvc.model.UserModel;

@RestController
public class UserRestController {
 
	@Autowired
	IUsersLogic usersLogic;
    
   
}