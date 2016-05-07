package com.springmvc.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.exceptions.HttpNotFoundException;
import com.springmvc.exceptions.HttpUnauthorizedException;

@Controller
public class MainController {

	@Value("${AppServer}")
	private String AppServer;
	
//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String getEntryPage(@RequestHeader HttpHeaders headers) 
//	{
//		System.out.println("====>Entry /");
//		return "entry";    	
//	}
	
	@RequestMapping(value = "/{tenantid}", method = RequestMethod.GET)
	public String getEntryPageTenant(@RequestHeader HttpHeaders headers, @PathVariable String tenantid) 
	{
		System.out.println("====>Entry /" + tenantid);
		if(TenantExist(tenantid))
		{
			return "entry";			
		}
		else
		{
			throw new HttpNotFoundException();
		}
	}
	
	@RequestMapping(value = "/{tenantid}/index", method = RequestMethod.GET)
	public String getIndexPage(@RequestHeader HttpHeaders headers, @PathVariable(value="tenantid") String tenantid) 
	{
		if(IsAuthenticated(headers, tenantid))
	    {
	    	return "index";	    	
	    }
	    else
	    {
    		return "login";
	    }
	}
	
	@RequestMapping(value = "/{tenantid}/pages/{page}", method = RequestMethod.GET)
	public String getPages(@RequestHeader HttpHeaders headers, @PathVariable(value="tenantid") String tenantid, @PathVariable(value="page") String page) 
	{
		if(IsAuthenticated(headers, tenantid))
		{
			return "pages/" + page;
		}
		else 
		{
			throw new HttpUnauthorizedException();
	    }
	}
	
	boolean TenantExist(String tenantid)
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(AppServer + tenantid +"/tenantExist");
		getRequest.addHeader("accept", "application/json");
		int responseCode = 401;//Default Unauthorize
		
		try 
		{
			HttpResponse response = httpClient.execute(getRequest);
			responseCode = response.getStatusLine().getStatusCode();
		} 
		catch (IOException e) 
		{
		}
		httpClient.getConnectionManager().shutdown();
		return responseCode != 404;
	}
	
	boolean IsAuthenticated(HttpHeaders headers, String tenantid)
	{
		String token = headers.getFirst("Authorization");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(AppServer + tenantid + "/isAuthenticated");
		getRequest.addHeader("accept", "application/json");
		getRequest.addHeader("Authorization", token);
		int responseCode = 401;//Default Unauthorize
		
		try 
		{
			HttpResponse response = httpClient.execute(getRequest);
			responseCode = response.getStatusLine().getStatusCode();
		} 
		catch (IOException e) 
		{
		}
		httpClient.getConnectionManager().shutdown();
		return responseCode == 200;
	}
}