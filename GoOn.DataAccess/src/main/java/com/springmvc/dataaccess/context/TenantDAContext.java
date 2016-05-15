package com.springmvc.dataaccess.context;

import com.springmvc.dataacces.config.TenantDataSourceConfig;
import com.springmvc.dataaccess.repository.tenant.UserRepository;
import com.springmvc.dataaccess.repository.tenant.ConfiguracionGlobalRepository;
import com.springmvc.dataaccess.repository.tenant.PagoRepository;
import com.springmvc.dataaccess.repository.tenant.VehiculoRepository;


public class TenantDAContext extends TenantDataSourceConfig {

	public UserRepository UserRepository;
	public ConfiguracionGlobalRepository ConfiguracionGlobalRepository;
	public PagoRepository PagoRepository;
	public VehiculoRepository VehiculoRepository;
	
	public TenantDAContext(String tenantName)
	{
		super(tenantName);
		UserRepository = new UserRepository(super.EntityManager);
		ConfiguracionGlobalRepository = new ConfiguracionGlobalRepository(super.EntityManager);
		PagoRepository = new PagoRepository(super.EntityManager);
		VehiculoRepository = new VehiculoRepository(super.EntityManager);
	}
}
