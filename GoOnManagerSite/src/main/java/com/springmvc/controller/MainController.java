package com.springmvc.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.springmvc.exceptions.HttpUnauthorizedException;

@Controller
public class MainController {

	@Value("${AppServer}")
	private String AppServer;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String geteNTRYPage(@RequestHeader HttpHeaders headers) 
	{
		return "entry";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String getIndexPage(@RequestParam String token) 
	{
		if(IsAuthenticated(token))
	    {
	    	return "index";
	    }
	    else
	    {
    		return "login";
	    }
	}
	
	@RequestMapping(value = "/pages/{page}", method = RequestMethod.GET)
	public String getPages(@RequestHeader HttpHeaders headers, @PathVariable(value="page") String page) 
	{
		if(IsAuthenticated(headers))
		{
			return "pages/" + page;
		}
		else 
		{
			throw new HttpUnauthorizedException();
	    }
	}
	
	boolean IsAuthenticated(String token)
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(AppServer + "isAuthenticated");
		getRequest.addHeader("accept", "application/json");
		getRequest.addHeader("Authorization", token);
		getRequest.addHeader("AppId", "MainAPP");
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
	
	boolean IsAuthenticated(HttpHeaders headers)
	{
		String token = headers.getFirst("Authorization");
		return IsAuthenticated(token);
	}
}