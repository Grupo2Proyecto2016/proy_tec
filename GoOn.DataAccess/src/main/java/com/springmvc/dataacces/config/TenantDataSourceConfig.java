package com.springmvc.dataacces.config;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

public class TenantDataSourceConfig 
{
	
	public TenantDataSourceConfig(String tenantName)
	{
		this.TenantName = tenantName.toUpperCase();
		try 
		{
			this.EntityManager = CreateTenantEntityManager();
		}
		catch (PropertyVetoException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected EntityManager EntityManager;
	
	private String TenantName = null;
	
	//@Value("${MainDatasourceDriverClassName}")
	private String MainDatasourceDriverClassName = "org.postgresql.Driver";
	
	//@Value("${MainDatasourceUsername}")
	private String MainDatasourceUsername ="postgres";
	
	//@Value("${MainDatasourcePassword}")
	private String MainDatasourcePassword = "root";
	
	private String DatasourceBaseUrl= "jdbc:postgresql://localhost:5432/";	

    public EntityManager CreateTenantEntityManager() throws PropertyVetoException
	{
    	Map<String, String> properties = new HashMap<>();
    	properties.put("hibernate.connection.url" , DatasourceBaseUrl + TenantName);
    	properties.put("hibernate.connection.driver_class" , MainDatasourceDriverClassName);
    	properties.put("hibernate.connection.username" , MainDatasourceUsername);
    	properties.put("hibernate.connection.password" , MainDatasourcePassword);
    	properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
    	properties.put("hibernate.hbm2ddl.auto", "update");
    	
    	EntityManagerFactory temf = Persistence.createEntityManagerFactory("temf", properties);
    	
    	return temf.createEntityManager(properties);
    }

}
