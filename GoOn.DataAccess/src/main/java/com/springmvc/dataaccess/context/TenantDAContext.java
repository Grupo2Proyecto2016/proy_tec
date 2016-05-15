package com.springmvc.dataaccess.context;

import com.springmvc.dataacces.config.TenantDataSourceConfig;
import com.springmvc.dataaccess.repository.tenant.UserRepository;
import com.springmvc.dataaccess.repository.tenant.ConfiguracionGlobalRepository;
import com.springmvc.dataaccess.repository.tenant.PagoRepository;
import com.springmvc.dataaccess.repository.tenant.VehiculoRepository;
import com.springmvc.dataaccess.repository.tenant.AsientoRepository;
import com.springmvc.dataaccess.repository.tenant.ParadaRepository;
import com.springmvc.dataaccess.repository.tenant.LineaRepository;


public class TenantDAContext extends TenantDataSourceConfig {

	public UserRepository UserRepository;
	public ConfiguracionGlobalRepository ConfiguracionGlobalRepository;
	public PagoRepository PagoRepository;
	public VehiculoRepository VehiculoRepository;
	public AsientoRepository AsientoRepository;
	public ParadaRepository ParadaRepository; 
	public LineaRepository LineaRepository;
	
	public TenantDAContext(String tenantName)
	{
		super(tenantName);
		UserRepository = new UserRepository(super.EntityManager);
		ConfiguracionGlobalRepository = new ConfiguracionGlobalRepository(super.EntityManager);
		PagoRepository = new PagoRepository(super.EntityManager);
		VehiculoRepository = new VehiculoRepository(super.EntityManager);
		AsientoRepository = new AsientoRepository(super.EntityManager);
		ParadaRepository = new ParadaRepository(super.EntityManager);
		LineaRepository = new LineaRepository(super.EntityManager);
	}
}
