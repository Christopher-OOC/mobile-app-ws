package com.appdeveloperblog.app.security;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

@Component
public class AuthorizationFilter extends BasicAuthenticationFilter {
	
	private UserRepository userRepository;
	
	public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			// pass it on to the next chain in line
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if (authorizationHeader == null) {
			return null;
		}
		
		String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
		
		byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
		
		JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey).build();
		
		
		Jwt<?, ?> jwt = jwtParser.parse(token);
		Claims payLoad = (Claims)jwt.getPayload();
		String email = payLoad.getSubject();
		System.out.println(email);
		
		if (email != null) {
			
			UserEntity userEntity = userRepository.findByEmail(email);
			
			if (userEntity == null) {
				return null;
			}
			
			UserPrincipal userPrincipal = new UserPrincipal(userEntity);
			
			return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
		}
		
		
		return null;
	}
	

}
