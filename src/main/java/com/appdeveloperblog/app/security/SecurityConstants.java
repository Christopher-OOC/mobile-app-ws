package com.appdeveloperblog.app.security;

import org.springframework.core.env.Environment;


import com.appdeveloperblog.app.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000;
	
	public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";
	
	public static final String SIGN_UP_URL = "/users";
	
	public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
		
	public static String getTokenSecret() {
		Environment environment = (Environment) SpringApplicationContext.getBean("environment");
		
		return environment.getProperty("tokenSecret");
	}

}
