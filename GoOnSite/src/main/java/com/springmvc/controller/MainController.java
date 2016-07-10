package com.springmvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springmvc.exceptions.HttpNotFoundException;
import com.springmvc.exceptions.HttpUnauthorizedException;

@Controller
public class MainController {

	@Value("${AppServer}")
	private String AppServer;
	
	List<String> allowedPages;
	
	public MainController()
	{
		allowedPages = new ArrayList();
		allowedPages.add("home");
		allowedPages.add("outbranches");
		allowedPages.add("travels");
		allowedPages.add("register");
	}
	
	@RequestMapping(value = "/{tenantid}", method = RequestMethod.GET)
	public String getEntryPageTenant(@RequestHeader HttpHeaders headers, @PathVariable String tenantid) 
	{
		System.out.println("====>Entry /" + tenantid);
		if(TenantExist(tenantid))
		{
			return "index";			
		}
		else
		{
			throw new HttpNotFoundException();
		}
	}
	
	@RequestMapping(value = "/{tenantid}/pages/{page}", method = RequestMethod.GET)
	public String getPages(@RequestParam(value="paymentId", required=false, defaultValue="") String paymentId,
						   @RequestParam(required=false, defaultValue="") String tokenPP,
						   @RequestParam(value="PayerID", required=false, defaultValue="") String PayerID,
			               @RequestHeader HttpHeaders headers, @PathVariable(value="tenantid") String tenantid, 
						   @PathVariable(value="page") String page, HttpServletResponse response,
						   Model model) 
	{
		
		String token = headers.getFirst("Authorization");
		String newToken = RefreshToken(token, tenantid);
		if(newToken != null)
		{
			response.setHeader("Authorization", newToken);
		}
		if(newToken == null && !allowedPages.contains(page))//not signed
		{
			throw new HttpUnauthorizedException();
		}
		
		model.addAttribute("paymentId", paymentId);
		model.addAttribute("tokenPP", tokenPP);
		model.addAttribute("PayerID", PayerID );
		
		return "pages/" + page;
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
		return RefreshToken(token, tenantid) != null;
	}
	
	String RefreshToken(String token, String tenantid)
	{
		String newToken = null;
		
		if(token != null && tenantid != null)
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(AppServer + tenantid + "/isAuthenticated");
			getRequest.addHeader("accept", "application/json");
			getRequest.addHeader("Authorization", token);
			int responseCode = 401;//Default Unauthorize
			
			try 
			{
				HttpResponse response = httpClient.execute(getRequest);
				Header[] authHeaders = response.getHeaders("Authorization");
				if(authHeaders.length > 0)
				{
					newToken = authHeaders[0].getValue();
				}
			} 
			catch (IOException e) 
			{
			}
			httpClient.getConnectionManager().shutdown();
		}
		return newToken;
	}
}