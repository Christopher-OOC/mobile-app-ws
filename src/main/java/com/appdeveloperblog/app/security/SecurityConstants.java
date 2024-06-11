package com.appdeveloperblog.app.security;

import org.springframework.core.env.Environment;

import com.appdeveloperblog.app.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";
	
	public static final String SIGN_UP_URL = "/users";
	
	public static final String TOKEN_SECRET = "hgh6JUjhhY78HGTy5HGvhg7jkLUIBghff6gfjbGyfHUgfJdYtysh8gs97g89h";
	
	public static String getTokenSecret() {
		Environment environment = (Environment) SpringApplicationContext.getBean("environment");
		
		return environment.getProperty("tokenSecret");
	}

}
