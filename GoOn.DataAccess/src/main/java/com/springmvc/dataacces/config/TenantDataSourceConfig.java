package com.springmvc.dataacces.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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
	private static final String CONFIG_PATH = "classpath*:application.properties";  
	
	static private Properties properties;
	
	protected EntityManager EntityManager;
	
	private String TenantName = null;

	public TenantDataSourceConfig(String tenantName)
	{
		if(properties == null)
		{
			LoadProperties();
		}
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

    public EntityManager CreateTenantEntityManager() throws PropertyVetoException
	{
    	Map<String, String> props = new HashMap<>();
    	props.put("hibernate.connection.url" , properties.get("dbServer") + TenantName);
    	props.put("hibernate.connection.driver_class" , (String) properties.get("driver-class-name"));
    	props.put("hibernate.connection.username" , (String) properties.get("mainDataSourceUsername"));
    	props.put("hibernate.connection.password" , (String) properties.get("mainDataSourcePassword"));
    	props.put("hibernate.dialect", (String) properties.get("postgresDialect"));
    	props.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); 
    	props.put("hibernate.hbm2ddl.auto", "update");
    	EntityManagerFactory temf = Persistence.createEntityManagerFactory("temf", props);
    	
    	return temf.createEntityManager();
    }

    private void LoadProperties()
    {
    	Resource resource = new ClassPathResource("/application.properties");
		try 
		{
			properties = PropertiesLoaderUtils.loadProperties(resource);
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
