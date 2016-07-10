package com.springmvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.springmvc.entities.tenant.Pasaje;
import com.springmvc.entities.tenant.Usuario;
import com.springmvc.logic.implementations.LinesLogic;
import com.springmvc.requestWrappers.CustomPayPalResponseWrapper;
import com.springmvc.requestWrappers.PayPalWrapper;
import com.springmvc.utils.UserContext;

@RestController
@RequestMapping(value = "/{tenantid}")
public class PayPalRestController 
{
	
	@Autowired
    private UserContext context;
	
	@Secured({"ROLE_CLIENT"})
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
		redirectUrls.setCancelUrl("http://localhost:8080/GoOnSite/" + tenantid + "#/payPalError");
		redirectUrls.setReturnUrl("http://localhost:8080/GoOnSite/" + tenantid + "#/payPalCheckout");
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
		return createdPayment.toJSON();
    }

	@Secured({"ROLE_CLIENT"})
	@RequestMapping(value = "/payPaypal", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<CustomPayPalResponseWrapper> payPaypal(@RequestBody PayPalWrapper paypal, @PathVariable String tenantid, HttpServletRequest request)
    {
		
		//reservar pasajes, si no se puede no sigo //llamando a clientreservetickets
		
		Usuario currentUser = context.GetUser(request, tenantid);		
		LinesLogic ll = new LinesLogic(tenantid);
		List<Pasaje> tickets = null;
		tickets = ll.ClientReserveTickets(currentUser, paypal.id_viaje, paypal.origen, paypal.destino, paypal.valor, paypal.seleccionados);
		
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
		Payer payer = new Payer();
		
		Payment payment = new Payment("sale", payer);
		payment.setId(paypal.paymentId);
		
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(paypal.PayerID);
		
		Payment pagoRealizado = null;
		
		try 
		{
			pagoRealizado = payment.execute(apiContext, paymentExecute);
		} 
		catch (PayPalRESTException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CustomPayPalResponseWrapper respuesta = new CustomPayPalResponseWrapper();
		//si el pago quedó cambio el estado a pagado de los pasajes reservados		
		if (pagoRealizado.getState().equals("approved"))
		{
			//pagoRealizado.getId(); //id del pago realizado
			try 
			{
				ll.ClientConfirmTickets(tickets, pagoRealizado.getId());
				respuesta.setSuccess(true);
				respuesta.setTickets(tickets);
			}
			catch (Exception e) 
			{
				//Rolbackear todos los pasajes
				e.printStackTrace();
				respuesta.setSuccess(false);
			}
			
		}
		else
		{
			respuesta.setSuccess(false);
			respuesta.setMsg("Ha ocurrido un error al realizar el pago");		
		}
		pagoRealizado.toJSON();	
		return new ResponseEntity<CustomPayPalResponseWrapper>(respuesta, HttpStatus.OK);
    }
	
}
