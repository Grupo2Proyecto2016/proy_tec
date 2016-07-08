package com.springmvc.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.springmvc.entities.main.Authority;
import com.springmvc.entities.main.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(com.springmvc.entities.tenant.Usuario user) {
        return new JwtUser(
                user.getIdUsuario(),
                user.getUsrname(),
                user.getNombre(),
                user.getApellido(),
                user.getPasswd(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.getEnabled(),
                user.getUltimoResetPassword()
        );
    }

    public static JwtUser create(com.springmvc.entities.main.Usuario user) {
        return new JwtUser(
                user.getIdUsuario(),
                user.getUsrname(),
                user.getNombre(),
                user.getApellido(),
                user.getPasswd(),
                mapToMainGrantedAuthorities(user.getAuthorities()),
                user.getEnabled(),
                user.getUltimoResetPassword()
        );
    }
    
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<com.springmvc.entities.tenant.Authority> authorities) 
    {
    	if(authorities == null || authorities.size() == 0)
    	{
    		return new ArrayList<>();
    	}
    	else
    	{
    		return authorities.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
				.collect(Collectors.toList());
    	}
    }
    
    private static List<GrantedAuthority> mapToMainGrantedAuthorities(List<com.springmvc.entities.main.Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
