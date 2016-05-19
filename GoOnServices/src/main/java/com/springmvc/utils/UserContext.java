package com.springmvc.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springmvc.configuration.JwtTokenUtil;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.logic.implementations.UsersLogic;

@Component
public class UserContext 
{
	private String tokenHeader = "Authorization";
	 
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	public Usuario GetUserFromRequest(HttpServletRequest request)
	{
		String token = request.getHeader(tokenHeader);
        if(token != null)
        {
        	String username = jwtTokenUtil.getUsernameFromToken(token);
        	String tenantId = jwtTokenUtil.getTenantFromToken(token);
        	return new UsersLogic(tenantId).GetUserByName(username);
        }
        return null;
	}
}
