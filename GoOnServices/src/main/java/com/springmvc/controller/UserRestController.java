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
    
    //-------------------Retrieve All Users--------------------------------------------------------
     
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<UserModel>> listAllUsers() {
        List<Usuario> users = usersLogic.findAllUsers();
        List<UserModel> userModels = ToUserModels(users);
        if(userModels.isEmpty()){
            return new ResponseEntity<List<UserModel>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<UserModel>>(userModels, HttpStatus.OK);
    }
 
 
    
    //-------------------Retrieve Single User--------------------------------------------------------
     
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUser(@PathVariable("id") long id) {
        System.out.println("Fetching User with id " + id);
        Usuario user = usersLogic.findById(id);
        
        if (user == null) 
        {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
        UserModel userModel = new UserModel(user);
        return new ResponseEntity<UserModel>(userModel, HttpStatus.OK);
    }
 
     
     
    //-------------------Create a User--------------------------------------------------------
     
    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody UserModel user,    UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + user.getUsername());
 
        if (usersLogic.isUserExist(user.getUsername())) 
        {
            System.out.println("A User with name " + user.getUsername() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        usersLogic.saveUser(user.ToEntity());
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
 
    
     
    //------------------- Update a User --------------------------------------------------------
     
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> updateUser(@PathVariable("id") long id, @RequestBody UserModel user) {
        System.out.println("Updating User " + id);
         
        Usuario currentUser = usersLogic.findById(id);
         
        if (currentUser==null)
        {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
 
        currentUser.setUsrname(user.getUsername());
        //currentUser.setAddress(user.getAddress());
        currentUser.setEmail(user.getEmail());
         
        usersLogic.updateUser(currentUser);
        return new ResponseEntity<UserModel>(new UserModel(currentUser), HttpStatus.OK);
    }
 
    
    
    //------------------- Delete a User --------------------------------------------------------
     
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserModel> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);
 
        Usuario user = usersLogic.findById(id);
        if (user == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
 
        usersLogic.deleteUserById(id);
        return new ResponseEntity<UserModel>(HttpStatus.NO_CONTENT);
    }
    
    private List<UserModel> ToUserModels(List<Usuario> users)
    {
    	List<UserModel> userModels = new ArrayList<UserModel>();
        for (Usuario user : users) 
        {
			UserModel mUser = new UserModel(user);
			userModels.add(mUser);
        }
        return userModels;
    }
}