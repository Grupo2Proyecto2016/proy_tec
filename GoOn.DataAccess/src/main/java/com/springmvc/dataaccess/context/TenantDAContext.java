package com.springmvc.dataaccess.context;

import com.springmvc.dataacces.config.TenantDataSourceConfig;
import com.springmvc.dataaccess.repository.tenant.UserRepository;;

public class TenantDAContext extends TenantDataSourceConfig {

	public UserRepository UserRepository;
	
	public TenantDAContext(String tenantName)
	{
		super(tenantName);
		UserRepository = new UserRepository(super.EntityManager);
	}
}
