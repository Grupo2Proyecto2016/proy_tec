package com.springmvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springmvc.logic.implementations.TenantLogic;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.logic.interfaces.ITenantLogic;
import com.springmvc.logic.interfaces.IUsersLogic;

/**
 * Created by stephan on 20.03.16.
 */
@Service
@ComponentScan(basePackages = "com.springmvc.logic.implementations")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	public String tenant;
	public boolean isMainApp;
	
	@Autowired
	ITenantLogic TenantLogic;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JwtUser jwtUser = null;
		com.springmvc.entities.tenant.Usuario tenantUser = null;
		com.springmvc.entities.main.Usuario user = null;
		
		if(tenant != null)
		{
			tenantUser = new UsersLogic(tenant).GetUserByName(username);
			if (tenantUser == null) 
	        {
	            throw new UsernameNotFoundException(String.format("No user found with username '%s' for tenant.", username));
	        }
			else 
	        {
	            return JwtUserFactory.create(tenantUser);
	        }
		}
		else if(isMainApp)
		{
			user = TenantLogic.GetUserByName(username);
			if (user == null) 
	        {
	            throw new UsernameNotFoundException(String.format("No user found with username '%s' for main app.", username));
	        }
			else 
	        {
	            return JwtUserFactory.create(user);
	            
	        }
		}
        return jwtUser;
	}
}
