package com.appdeveloperblog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@ComponentScan(value="com.appdeveloperblog.app.security")
@ComponentScan(value="com.appdeveloperblog.app.ws.ui.controller")
@ComponentScan(value="com.appdeveloperblog.app.service.impl")
@ComponentScan(value="com.appdeveloperblog.app.shared")
@EntityScan(value="com.appdeveloperblog.app.io.entity")

@SpringBootApplication
public class Practice2Application {

	public static void main(String[] args) {
		SpringApplication.run(Practice2Application.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
}
