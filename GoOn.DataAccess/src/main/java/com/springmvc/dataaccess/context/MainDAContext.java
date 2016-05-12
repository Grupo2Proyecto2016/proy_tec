package com.springmvc.dataaccess.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springmvc.dataaccess.repository.main.UserRepository;
import com.springmvc.entities.main.Usuario;

@Component
@Configuration
public class MainDAContext {

	@Autowired
	DataSource mainDataSource;
	
	@Autowired UserRepository usersRepository;
	
	public Usuario GetUserByName(String userName)
	{
		return usersRepository.findByUsrname(userName);
	}
	
	public String GetTenant(String tenantName)
	{
		String result = null;
		try 
		{
			Connection c = mainDataSource.getConnection();
			java.sql.Statement statement = null;
			statement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery("select nombre_tenant from empresa where UPPER(nombre_tenant) = UPPER('" + tenantName + "')");			
			if(rs.first())
			{
				result = rs.getString("nombre_tenant");				   
			}
		}
		catch (SQLException e)
		{
			return result;
		}		
		return result;
    }
	
	@Transactional
    public void CreateTenant(String nombreTenant)
	{
		try 
		{
			Connection c = mainDataSource.getConnection();
			java.sql.Statement statement = null;
			statement = c.createStatement();
			statement.executeUpdate("CREATE DATABASE " + nombreTenant);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
