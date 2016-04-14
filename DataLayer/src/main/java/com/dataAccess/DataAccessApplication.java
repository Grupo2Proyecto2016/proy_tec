package com.dataAccess;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class DataAccessApplication {
	
	
	private static final Logger log = LoggerFactory.getLogger(DataAccessApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(DataAccessApplication.class);
	}
	
	@Bean
	public CommandLineRunner adfg(EmpresaRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Empresa(1,"Nosar"));
			repository.save(new Empresa(2,"Cita"));
			repository.save(new Empresa(3,"Cot"));		
			

			// fetch all customers
			log.info("Empresas usando findAll():");
			log.info("-------------------------------");
			for (Empresa empresa : repository.findAll()) {
				log.info(empresa.toString());
			}
            log.info("");

			// fetch an individual customer by ID
			Empresa empresa = repository.findOne(1L);
			log.info("Empresas usando findOne(1L):");
			log.info("--------------------------------");
			log.info(empresa.toString());
            log.info("");

			// fetch customers by last name
			log.info("Empresas usando findByLastName('Nosar'):");
			log.info("--------------------------------------------");
			for (Empresa bauer : repository.findByNombre("Nosar")) {
				log.info(bauer.toString());
			}
            log.info("");
		};
	}
}
