package com.springmvc.configuration;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import com.springmvc.entities.main.Empresa;
import com.springmvc.logic.interfaces.ITenantLogic;

@Component
@ComponentScan("com.springmvc.logic")
public class ReservationChecker extends Thread
{
	@Autowired 
    private ITenantLogic tenantLogic;
	
	public void run()
	{
		while(!Thread.currentThread().isInterrupted()) 
		{
			List<Thread> tasks = new ArrayList<Thread>();
			List<Empresa> companies =  tenantLogic.GetCompanies();
			for (Empresa company : companies) 
			{
				Thread task = new ReservationCheckerTask(company.getNombreTenant());
				task.start();
			}
			
			for (Thread task : tasks)
			{
				try 
				{
					task.wait();
				}
				catch (InterruptedException e) 
				{
					e.printStackTrace();
					break;
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			try 
			{
				Thread.sleep(60000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				break;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
