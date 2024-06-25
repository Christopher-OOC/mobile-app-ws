package com.appdeveloperblog.app.security;

import java.io.IOException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.appdeveloperblog.app.SpringApplicationContext;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

@SuppressWarnings("deprecation")
@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {

		try {
			
			UserLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(),
					UserLoginRequestModel.class);
			
			System.out.println(creds);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("HERE 1");
		byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
		Instant now = Instant.now();

		String username = ((UserPrincipal) authResult.getPrincipal()).getUsername();
		String token = Jwts.builder().setSubject(username)
				.setExpiration(Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME))).setIssuedAt(Date.from(now))
				.signWith(secretKey, SignatureAlgorithm.HS512).compact();

		System.out.println("HERE 1");
		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
		System.out.println("HERE 2");
		UserEntity userDto = userService.findByEmail(username);

		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		response.addHeader("userId", userDto.getUserId());
	}
}
