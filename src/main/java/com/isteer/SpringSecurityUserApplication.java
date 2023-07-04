package com.isteer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityUserApplication {

	private static final Logger LOGGER=LogManager.getLogger("CommonLogger");
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityUserApplication.class, args);
		
		LOGGER.info("Application started sucessfully");;
		
		

	}

}
