package com.isteer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityUserApplication.class, args);
		
		Map<String,Boolean> m=new HashMap<>();
		System.out.println(m.get("useremail"));
	}

}
