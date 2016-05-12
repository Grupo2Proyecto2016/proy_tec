package com.springmvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private String tokenHeader = "Authorization";
    private String mainAppHeader = "AppId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
    	boolean isMainApp = false;
    	HttpServletRequest req = (HttpServletRequest)request;
    	String path = req.getRequestURI().substring(req.getContextPath().length());
    	String reqTenant = null;
    	String[] urlParts = path.split("/");
    	if(urlParts.length > 2)
    	{
    		reqTenant = urlParts[1];
    	}
        
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(this.tokenHeader);
        String appIdHeader = httpRequest.getHeader(this.mainAppHeader);
        if(appIdHeader != null)
        {
        	isMainApp = appIdHeader.equals("MainAPP");
        }
        
        userDetailsService.tenant = reqTenant;
        userDetailsService.isMainApp = isMainApp;
        
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        String tenant = jwtTokenUtil.getTenantFromToken(authToken);
        
        //ESTA VALIDACION SE TIENE QUE HACER CUANDO ESTE ANDANDO
        if (username != null && (isMainApp ||(tenant != null && reqTenant != null && reqTenant.equals(tenant))) && SecurityContextHolder.getContext().getAuthentication() == null) 
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(authToken, userDetails, tenant, isMainApp)) 
            {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}