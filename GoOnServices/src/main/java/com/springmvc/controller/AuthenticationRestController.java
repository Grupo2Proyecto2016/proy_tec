package com.springmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.springmvc.configuration.JwtAuthenticationRequest;
import com.springmvc.configuration.JwtAuthenticationResponse;
import com.springmvc.configuration.JwtTokenUtil;
import com.springmvc.configuration.JwtUser;
import com.springmvc.configuration.JwtUserDetailsServiceImpl;
import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.exceptions.HttpNotFoundException;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.logic.interfaces.ITenantLogic;
import com.springmvc.requestWrappers.TenantAuthWrapper;
import com.springmvc.utils.UserContext;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/{tenantid}")
public class AuthenticationRestController {

    private String tokenHeader = "Authorization";
    
    @Autowired
    private UserContext context;
    
    @Autowired 
    private ITenantLogic tenantLogic;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    
    @RequestMapping(value = "isAuthenticated", method = RequestMethod.GET)
    public String IsAuthenticated()
    {
		return "";
    }
    
    @RequestMapping(value = "/tenantExist", method = RequestMethod.GET)
    public String tenantExist(@PathVariable String tenantid) throws AuthenticationException 
    {
    	boolean tenantExist = tenantLogic.TenantExists(tenantid);
    	if(tenantExist)
    	{
    		return "";    		
    	}
    	else
    	{
    		throw new HttpNotFoundException();
    	}
    }
    
    @RequestMapping(value = "/getCompany", method = RequestMethod.GET)
    public ResponseEntity<Empresa>  getCompany(@PathVariable String tenantid)
    {
    	Empresa company = tenantLogic.GetCompany(tenantid);
    	return new ResponseEntity<Empresa>(company, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseEntity<?>  getUserInfo(@PathVariable String tenantid, HttpServletRequest request)
    {
    	Usuario user = context.GetUser(request);
    	if(user == null)
    	{
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	else
    	{
    		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
    	}
    }
    
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@PathVariable String tenantid, @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException 
    {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, tenantid, false);

        // Return the token and user info
        TenantAuthWrapper response = new TenantAuthWrapper();
        response.user = new UsersLogic(tenantid).GetUserByName(authenticationRequest.getUsername());
        response.token = token;
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
