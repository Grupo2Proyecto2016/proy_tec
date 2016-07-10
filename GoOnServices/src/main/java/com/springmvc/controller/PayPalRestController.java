package com.springmvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class PayPalRestController 
{
	
	@Autowired
    private UserContext context;
	
	@RequestMapping(value = "/getPaypal", method = RequestMethod.GET, /*consumes="application/json",*/ produces = "application/json")
	@ResponseBody
	public String getPayPal(@PathVariable String tenantid)
    {
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		String accessToken = null;
		try 
		{
			accessToken = new OAuthTokenCredential("AcYJ0yuY-V2vxt6XETFzitK6qBADBO9_XEiXZov3iCv-4S4RnCAywVBIAcPfRLsMghjPDz-bZASB_Efz", "ED5WkjGVLaTYkia-tzl6FkyA6w3sqz8dk8wSrRvmlK4UFnQO6ytTwjbGdouFe2DPuKR9yNj59vY1V688", sdkConfig).getAccessToken();
		} 
		catch (PayPalRESTException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		APIContext apiContext = new APIContext(accessToken);
		apiContext.setConfigurationMap(sdkConfig);

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("12");
		
		Transaction transaction = new Transaction();
		transaction.setDescription("Pago de pasaje GoOn");
		transaction.setAmount(amount);
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("https://devtools-paypal.com/guide/pay_paypal?cancel=true");
		redirectUrls.setReturnUrl("https://devtools-paypal.com/guide/pay_paypal?success=true");
		payment.setRedirectUrls(redirectUrls);

		Payment createdPayment = null;
		
		try 
		{
			createdPayment = payment.create(apiContext);			
		} 
		catch (PayPalRESTException e) 
		{
			createdPayment = null;
			e.printStackTrace();
		}
				
		return createdPayment.toString();
    }
	
}
