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
	
	public Usuario GetUser(HttpServletRequest request, String tenantId)
	{
		String token = request.getHeader(tokenHeader);
        if(token != null)
        {
        	String username = jwtTokenUtil.getUsernameFromToken(token);
        	Usuario user = new UsersLogic(tenantId).GetUserByName(username);
        	if(user != null)
        	{
        		boolean isValid = jwtTokenUtil.validateToken(token, user, tenantId);
        		if(isValid)
        		{
        			return user;
        		}
        	}
        }
        return null;
	}

	public String GetUsername(HttpServletRequest request) 
	{
		String token = request.getHeader(tokenHeader);	
		String username = jwtTokenUtil.getUsernameFromToken(token);
		return username;
	}
}
