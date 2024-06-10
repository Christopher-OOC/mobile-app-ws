package com.appdeveloperblog.app.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.appdeveloperblog.app.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

@Slf4j
public class AnthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AnthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
		
		try {
			UserLoginRequestModel creds = objectMapper.readValue(req.getInputStream(), UserLoginRequestModel.class);
			
			log.info("USER: {}", creds);
			
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} 
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.TOKEN_SECRET.getBytes());
		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
		Instant now = Instant.now();
		
		String username = ((User) authResult.getPrincipal()).getUsername();
		String token = Jwts.builder()
						.setSubject(username) 
						.setExpiration(
								Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
						.setIssuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();
	
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	}
}
