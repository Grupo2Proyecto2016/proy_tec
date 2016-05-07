package com.springmvc.dataacces.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.springmvc.dataaccess.repository.main", entityManagerFactoryRef = "emf")
public class MainDataSourceConfig
{
	//@Value("${MainDatasourceDriverClassName}")
	private String MainDatasourceDriverClassName = "org.postgresql.Driver";
	
	//@Value("${MainDatasourceUsername}")
	private String MainDatasourceUsername ="postgres";
	
	//@Value("${MainDatasourcePassword}")
	private String MainDatasourcePassword = "root";
	
	//@Value("${MainDatasourceUrl}")
	private String MainDatasourceUrl = "jdbc:postgresql://localhost:5432/MainApp";
	
	@Bean(name = { "mainDataSource" })
	public DataSource mainDataSource() 
	{
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(MainDatasourceDriverClassName);
		dataSource.setUrl(MainDatasourceUrl);
		dataSource.setUsername(MainDatasourceUsername);
		dataSource.setPassword(MainDatasourcePassword);
		return dataSource;
	}	
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
    public LocalContainerEntityManagerFactoryBean emf() throws PropertyVetoException {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(mainDataSource());
        emf.setPackagesToScan("com.springmvc.entities.main");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(additionalProperties());
        return emf;
    }
	
	Properties additionalProperties()
	{
		Properties properties = new Properties();
		//properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
		return properties;
	}

	
}
