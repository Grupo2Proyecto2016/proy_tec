package com.springmvc.controller;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
///import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.configuration.JwtAuthenticationRequest;
import com.springmvc.configuration.JwtAuthenticationResponse;
import com.springmvc.configuration.JwtTokenUtil;
import com.springmvc.configuration.JwtUser;
import com.springmvc.configuration.JwtUserDetailsServiceImpl;
import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.main.Pais;
import com.springmvc.entities.tenant.Rol;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.exceptions.HttpNotFoundException;
import com.springmvc.logic.implementations.TenantLogic;
import com.springmvc.logic.implementations.UsersLogic;
import com.springmvc.logic.interfaces.ITenantLogic;
import com.springmvc.logic.interfaces.IUsersLogic;
import com.springmvc.model.UserModel;
import com.springmvc.requestWrappers.CompanyWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TenantManagerRestController {

    private String tokenHeader = "Authorization";
    
    @Autowired 
    private ITenantLogic tenantLogic;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    
    @RequestMapping(value = "/tenantExist", method = RequestMethod.GET)
    public ResponseEntity<Void> tenantExist(@RequestParam(value="tenantId") String tenantId) throws AuthenticationException 
    {
    	boolean tenantExist = tenantLogic.TenantExists(tenantId);
    	if(tenantExist)
    	{
    		return new ResponseEntity<Void>(HttpStatus.OK);   		
    	}
    	else
    	{
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	}
    }
    
    @RequestMapping(value = "countries", method = RequestMethod.GET)
    public List<?> GetCountries()
    {
    	List<Pais> countries = tenantLogic.GetCountries();
    	return countries;
    }
    
    @RequestMapping(value = "createCompany", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
    public ResponseEntity<Void> CreateCompany(@RequestBody CompanyWrapper companyWrapper)
    {
    	Empresa company = new Empresa();
    	company.setNombre(companyWrapper.getName());
    	company.setDireccion(companyWrapper.getAddress());
    	company.setRut(companyWrapper.getRut());
    	company.setRazonSocial(companyWrapper.getTrueName());
    	company.setTelefono(companyWrapper.getPhone());
    	company.setNombreTenant(companyWrapper.getTenantName());
    	Pais country = tenantLogic.GetCountry(companyWrapper.getCountryId());
    	company.setPais(country);
    	
    	Usuario user = companyWrapper.user;
    	String hashedPass = new BCryptPasswordEncoder().encode(companyWrapper.getPassword());
    	user.setPasswd(hashedPass);
    	user.setEnabled(true);
    	user.setEs_empleado(true);
    	user.setPuede_crear(true);
    	user.setRol(null);
    	user.SetAuthorities(null);
    	user.setUltimoResetPassword(new Date());
    	tenantLogic.CreateTenant(company, user);
    	
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "isAuthenticated", method = RequestMethod.GET)
    public String IsAuthenticated()
    {
		return "";
    }
    
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException 
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
        final String token = jwtTokenUtil.generateToken(userDetails, null, true);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
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
