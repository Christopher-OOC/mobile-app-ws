package com.appdeveloperblog.app.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.appdeveloperblog.app.UserRepository;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Collections;

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
	protected UserDetailsService getUserDetailsService() {
		return args -> {
			UserEntity user = userRepository.findByEmail(args);

			if (user == null) {
				throw new UsernameNotFoundException("No user found with email: " + args);
			}

			return new User(user.getEmail(), user.getEncryptedPassword(), user.getEmailVerificationStatus(), true, true,
					true, Collections.emptyList());
		};
	}

	@Bean
	protected AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		return managerBuilder.build();
	}

	@Bean
	protected SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(request -> request
				.requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
				.requestMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL).permitAll()
				.anyRequest().authenticated()).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		AuthenticationFilter authenticationFilter = new AuthenticationFilter(getAuthenticationManager(http));
		authenticationFilter.setFilterProcessesUrl("/users/login");
		authenticationFilter.setUsernameParameter("email");

		AuthorizationFilter authorizationFilter = new AuthorizationFilter(getAuthenticationManager(http));

		http.addFilter(authenticationFilter);
		http.addFilter(authorizationFilter);

		return http.build();
	}
}
