package com.isteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.isteer.logs.Log4j2;

@SpringBootApplication
public class SpringSecurityUserApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityUserApplication.class, args);
		Log4j2.getLogger().info("Application started sucessfully");
		
		}
}
