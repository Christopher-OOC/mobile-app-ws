package com.appdeveloperblog.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.appdeveloperblog.app.UserRepository;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebSecurity {
	
	@Autowired
	private UserRepository userRepository;

	@Bean
	ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	UserDetailsService getUserDetailsService() {
		return args -> {
			UserEntity user = userRepository.findByEmail(args);
			
			if (user == null) {
				throw new UsernameNotFoundException("No user found with email: " + args);
			}
			
			log.info("User: {}", user);
			
			return new User(user.getEmail(), user.getEncryptedPassword(), Collections.emptyList());
		};
	}
	
	@Bean
	AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		
		return managerBuilder.build();
	}

	@Bean
	protected SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws  Exception {
		
		http
			.authorizeHttpRequests(request -> 
					request.requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
					.anyRequest().authenticated()
			)
			.csrf(csrf -> 
					csrf.disable()
			)
			.formLogin(login -> login
					.loginProcessingUrl("/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.permitAll()
			);
			
		http.addFilter(new AuthenticationFilter(getAuthenticationManager(http)));
			
		return http.build();
	}
}
