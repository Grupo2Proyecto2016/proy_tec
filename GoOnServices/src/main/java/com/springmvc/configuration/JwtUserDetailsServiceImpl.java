package com.springmvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.logic.interfaces.IUsersLogic;

/**
 * Created by stephan on 20.03.16.
 */
@Service
@ComponentScan(basePackages = "implementations")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	public String tenant;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.springmvc.entities.tenant.Usuario user = null;
		if(tenant != null)//TODO: Cambiar la condicion luego de las pruebas
		{
			user = new UsersLogic(tenant).GetUserByName(tenant, username);
		}
		else
		{
			//user = usersLogic.GetUserByName(username);
		}

		if (user == null) 
        {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } 
		else 
        {
            return JwtUserFactory.create(user);
        }
	}
}
