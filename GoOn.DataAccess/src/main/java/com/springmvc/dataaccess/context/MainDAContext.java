package com.springmvc.dataaccess.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springmvc.dataaccess.repository.main.CountryRepository;
import com.springmvc.dataaccess.repository.main.EmpresaRepository;
import com.springmvc.dataaccess.repository.main.UserRepository;
import com.springmvc.entities.main.Empresa;
import com.springmvc.entities.main.Pais;
import com.springmvc.entities.main.Usuario;

@Component
@Configuration
public class MainDAContext {

	@Autowired
	DataSource mainDataSource;
	
	@Autowired UserRepository usersRepository;
	@Autowired CountryRepository countryRepository;
	@Autowired EmpresaRepository companyRepository;
	
	public Usuario GetUserByName(String userName)
	{
		return usersRepository.findByUsrname(userName);
	}
	
	public List<Pais> GetCountries()
	{
		List<Pais> result = new ArrayList<>();
		Iterable<Pais> countries = countryRepository.findAll();
		countries.forEach(result::add);
		return result;
	}
	
	public String GetTenant(String tenantName)
	{
		String result = null;
		try 
		{
			Connection c = mainDataSource.getConnection();
			java.sql.Statement statement = null;
			statement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery("select nombretenant from empresa where LOWER(nombretenant) = LOWER('" + tenantName + "')");			
			if(rs.first())
			{
				result = rs.getString("nombretenant");				   
			}
		}
		catch (SQLException e)
		{
			return result;
		}		
		return result;
    }
	
	@Transactional
    public void CreateTenant(Empresa company)
	{
		String nombreTenant = company.getNombreTenant();
		try 
		{
			InsertCompany(company);
			Connection c = mainDataSource.getConnection();
			java.sql.Statement statement = null;
			statement = c.createStatement();
			statement.executeUpdate("CREATE DATABASE " + nombreTenant.toLowerCase());
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public Pais GetCountry(long countryId)
	{
		return countryRepository.findOne(countryId);
	}
	
	private void InsertCompany(Empresa company)
	{
		companyRepository.save(company);
	}

	public Empresa GetCompany(String tenantid) 
	{
		return companyRepository.findBynombretenant(tenantid);
	}
	
	public List<Empresa> GetCompanies() 
	{
		List<Empresa> result = new ArrayList<>();
		Iterable<Empresa> companies = companyRepository.findAll();
		companies.forEach(result::add);
		return result;
	}
}
