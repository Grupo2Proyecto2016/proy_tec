package com.springmvc.dataacces.config;

import org.osgi.service.component.annotations.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Component
public class JPAConfiguration
{
	@Value("${driver-class-name}")
	public String MainDatasourceDriverClassName;
	
	@Value("${mainDataSourceUsername}")
	public String MainDatasourceUsername;
	
	@Value("${mainDataSourcePassword}")
	public String MainDatasourcePassword = "masterkey";
	
	@Value("${mainDataSourceUrl}")
	public String MainDatasourceUrl;
}
