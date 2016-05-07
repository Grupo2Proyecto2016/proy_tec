package com.springmvc.logic.implementations;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springmvc.dataaccess.context.MainDAContext;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.main.Usuario;
import com.springmvc.logic.interfaces.IUsersLogic;

@Component
public class UsersLogic implements IUsersLogic {
	
	private static final AtomicLong counter = new AtomicLong();
	
	private TenantDAContext DAContext;
	
	public UsersLogic()
	{
	}
	
	public UsersLogic(String tenant)
	{
		DAContext = new TenantDAContext(tenant);
	}
	
	@Autowired MainDAContext dataContext;
	
	private static List<Usuario> users;

	public List<Usuario> findAllUsers() {
		return users;
	}
	
	public Usuario findById(long id) {
		for(Usuario user : users){
//			if(user.getId() == id){
//				return user;
//			}
		}
		return null;
	}
	
	public com.springmvc.entities.tenant.Usuario GetUserByName(String tenant, String userName)
	{
		com.springmvc.entities.tenant.Usuario user = DAContext.UserRepository.FindByUsername(userName);
    	return user;
	}
	
	public Usuario GetUserByName(String userName)
	{
		Usuario user = dataContext.GetUserByName(userName);
    	return user;
	}
	
	public void saveUser(Usuario user) {
		user.setIdUsuario(counter.incrementAndGet());
		users.add(user);
	}

	public void updateUser(Usuario user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	public void deleteUserById(long id) {
		
		for (Iterator<Usuario> iterator = users.iterator(); iterator.hasNext(); ) {
			Usuario user = iterator.next();
		    if (user.getIdUsuario() == id) {
		        iterator.remove();
		    }
		}
	}

	public boolean isUserExist(String username) {
		return GetUserByName(null, username)!=null;
	}
	
	public void deleteAllUsers(){
		users.clear();
	}
}
