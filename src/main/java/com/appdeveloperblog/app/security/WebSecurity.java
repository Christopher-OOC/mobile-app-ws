package com.appdeveloperblog.app.security;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.appdeveloperblog.app.UserRepository;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebSecurity {

	
//	@Autowired
//	private AuthenticationManager authenticationManager;
	
//	@Autowired
//	private UserRepository userRepository;

	private UserService userService;
	
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	public WebSecurity(UserService userService, BCryptPasswordEncoder bcryptPasswordEncoder) {
		super();
		this.userService = userService;
		this.bcryptPasswordEncoder = bcryptPasswordEncoder;
	}



//	ObjectMapper getObjectMapper() {
//		return new ObjectMapper();
//	}
	
//	@Bean
//	BCryptPasswordEncoder bcryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
//	@Bean 
//	UserDetailsService getUserDetailsService() {
//		
//		return new UserServiceImpl();
//	}
//	

//	@Bean
//	UserDetailsService getUserDetailsService() {
//		return args -> {
//			UserEntity user = userRepository.findByEmail(args);
//			
//			if (user == null) {
//				log.info("Christopher Error:");
//				log.info("Username: {}", args);
//				log.info("User: {}", user);
//				throw new RuntimeException("No user found, OK");
//			}
//			
//			return new User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, Collections.emptyList());
//		};
//		
//	}

	@Bean
	protected SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws  Exception {
		
		// Get Authentication Builder
		
		http
			.authorizeHttpRequests(request -> 
					request.requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
					.anyRequest().authenticated()
			)
			.csrf(csrf -> 
					csrf.disable()
			)
			.formLogin(login -> login
					.loginProcessingUrl("/users/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.permitAll()
			);
		
		
		AuthenticationManager manager = http.getSharedObject(AuthenticationManagerBuilder.class)
		.userDetailsService(userService)
		.passwordEncoder(bcryptPasswordEncoder).and().build();
			
			
			//http.addFilter(new AnthenticationFilter(manager));
			
		return http.build();
	}
}
