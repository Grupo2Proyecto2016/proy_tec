package com.springmvc.dataacces.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import org.springframework.data.elasticsearch.core.EntityMapper;
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
	
	protected static HashMap<String,EntityManager> EntityManagers;
	
	protected EntityManager EntityManager;
	
	private String TenantName = null;
	
	private final Lock lock = new ReentrantLock();

	public TenantDataSourceConfig(String tenantName, boolean updateSchema)
	{
		if(properties == null)
		{
			LoadProperties();
		}
		this.TenantName = tenantName.toLowerCase();
		try 
		{
			this.EntityManager = CreateTenantEntityManager(updateSchema);
		}
		catch (PropertyVetoException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public EntityManager CreateTenantEntityManager(boolean updateSchema) throws PropertyVetoException
	{
    	if (EntityManagers == null)
    	{
    		EntityManagers = new HashMap<String, EntityManager>();
		}
    	
    	if(EntityManagers.containsKey(TenantName))
    	{
    		return EntityManagers.get(TenantName);
    	}
    	else
    	{
    		Map<String, String> props = new HashMap<>();
    		props.put("hibernate.connection.url" , properties.get("dbServer") + TenantName);
    		props.put("hibernate.connection.driver_class" , (String) properties.get("driver-class-name"));
    		props.put("hibernate.connection.username" , (String) properties.get("mainDataSourceUsername"));
    		props.put("hibernate.connection.password" , (String) properties.get("mainDataSourcePassword"));
    		props.put("hibernate.dialect", (String) properties.get("postgresDialect"));
    		props.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); 
    		if (updateSchema)
    		{
    			props.put("hibernate.hbm2ddl.auto", "update");
    		}
    		EntityManagerFactory temf = Persistence.createEntityManagerFactory("temf", props);
    		EntityManager em = temf.createEntityManager();
    		lock.lock();
    		EntityManagers.put(TenantName, em);
    		lock.unlock();
    		return em;
    	}    	
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
