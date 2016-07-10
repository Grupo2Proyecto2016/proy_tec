package com.springmvc.logic.implementations;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springmvc.dataaccess.context.MainDAContext;
import com.springmvc.dataaccess.context.TenantDAContext;
import com.springmvc.entities.main.Usuario;
import com.springmvc.enums.UserRol;
import com.springmvc.exceptions.UserAlreadyExistsException;
import com.springmvc.logic.interfaces.IUsersLogic;
import com.springmvc.logic.utils.MailMessages;
import com.springmvc.logic.utils.MailSender;

@Component
public class UsersLogic implements IUsersLogic {
	
	private static final AtomicLong counter = new AtomicLong();
	private String CurrentTenant;
	
	private TenantDAContext TenantContext;
	@Autowired MainDAContext dataContext;
	
	public UsersLogic(String tenant)
	{
		CurrentTenant = tenant;
		TenantContext = new TenantDAContext(tenant, false);
	}
	
	public UsersLogic(String tenant, boolean updateSchema)
	{
		CurrentTenant = tenant;
		TenantContext = new TenantDAContext(tenant, true);
	}
	
	public UsersLogic()
	{
	}
	
	public com.springmvc.entities.tenant.Usuario GetUserByName(String userName)
	{
		com.springmvc.entities.tenant.Usuario user = TenantContext.UserRepository.FindByUsername(userName);
    	return user;
	}
	
	public Usuario GetMainUserByName(String userName)
	{
		Usuario user = dataContext.GetUserByName(userName);
    	return user;
	}
	
	public void CreateUser(com.springmvc.entities.tenant.Usuario user) throws UserAlreadyExistsException
	{
		boolean userExists = TenantContext.UserRepository.FindByUsername(user.getUsrname()) != null;
		if(userExists)
		{
			throw new UserAlreadyExistsException(user.getUsrname());
		}
		else
		{
			
			TenantContext.UserRepository.InsertUser(user);
			if(user.getRol_id_rol() == UserRol.Admin.getValue())
			{
				MailSender.Send(
					user.getEmail(),
					MailMessages.TenantCreatedTitle,
					String.format(MailMessages.TenantCreatedMsg, user.getNombre(), user.getUsrname(), CurrentTenant)
				);
			}
			else if(user.getRol_id_rol() == UserRol.Client.getValue())
			{
				MailSender.Send(
					user.getEmail(),
					MailMessages.ClientCreatedTitle,
					String.format(MailMessages.ClientCreatedMsg, user.getNombre(), user.getUsrname(), CurrentTenant)
				);
			}
			else
			{
				MailSender.Send(
					user.getEmail(),
					MailMessages.EmployeeCreatedTitle,
					String.format(MailMessages.EmployeeCreatedMsg, user.getNombre(), user.getUsrname(), CurrentTenant)
				);
			}
		}
	}

	public com.springmvc.entities.tenant.Usuario GetTenantAdmin()
	{
		return TenantContext.UserRepository.FindByRole(UserRol.Admin);
	}

	public void SetUpRoles() 
	{
		TenantContext.RolRepository.SetUp();
	}

	public List<com.springmvc.entities.tenant.Usuario> GetEmployees() 
	{
		return TenantContext.UserRepository.GetEmployees();
	}

	public void UpdateUser(com.springmvc.entities.tenant.Usuario userUpdateData) 
	{
		TenantContext.UserRepository.UpdateUser(userUpdateData);
		if(userUpdateData.getRol_id_rol() == UserRol.Client.getValue())
		{
			TenantContext.EncomiendaRepository.UpdatePackagesCI(userUpdateData.getIdUsuario(), userUpdateData.getCi());
			TenantContext.PasajeRepository.UpdateTicketsCI(userUpdateData.getIdUsuario(), userUpdateData.getCi());
		}
	}

	public void DeleteUser(String usrname) 
	{
		TenantContext.UserRepository.DeleteUser(usrname);
	}

	public void UpdateUserPassword(com.springmvc.entities.tenant.Usuario signedUser, String newPassword) 
	{
		TenantContext.UserRepository.UpdateUserPassword(signedUser, newPassword);
	}

	public List<com.springmvc.entities.tenant.Usuario> GetEmployeesByRole(UserRol rol) 
	{
		return TenantContext.UserRepository.GetEmployeesByRol(rol);
	}

	public boolean ClientExists(String username) 
	{
		com.springmvc.entities.tenant.Usuario user = TenantContext.UserRepository.GetClient(username);
    	return user != null;
	}
}
