package com.appdeveloperblog.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.appdeveloperblog.app.SpringApplicationContext;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableMethodSecurity
public class WebSecurity {

	@Autowired
	private UserRepository userRepository;

	@Bean
	SpringApplicationContext getContext() {
		return new SpringApplicationContext();
	}

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
			UserEntity userEntity = userRepository.findByEmail(args);

			if (userEntity == null) {
				throw new UsernameNotFoundException("No user found with email: " + args);
			}

			return new UserPrincipal(userEntity);
		};
	}

	@Bean
	protected AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		return managerBuilder.build();
	}

	@Bean
	protected SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
				.permitAll().requestMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL).permitAll()
				.requestMatchers(HttpMethod.GET, SecurityConstants.PASSWORD_RESET_REQUEST_URL).permitAll()
				.requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("DELETE_AUTHORITY")
				.requestMatchers("/v3/api-docs*", "/configuration/**", "/swagger-ui/**", "/webjars/**").permitAll()
				.anyRequest().authenticated())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		AuthenticationFilter authenticationFilter = new AuthenticationFilter(getAuthenticationManager(http));
		authenticationFilter.setFilterProcessesUrl("/users/login");
		authenticationFilter.setUsernameParameter("email");
		authenticationFilter.setPasswordParameter("password");

		AuthorizationFilter authorizationFilter = new AuthorizationFilter(getAuthenticationManager(http), userRepository);

		http.addFilter(authenticationFilter);
		http.addFilter(authorizationFilter);

		return http.build();
	}
}
