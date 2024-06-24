package com.appdeveloperblog.app.shared;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.appdeveloperblog.app.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();

	private final String ALPHABET = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String generateUserId(int length) {
		return generateRandomString(length);
	}

	public String generateAddressId(int length) {
		return generateRandomString(length);
	}

	private String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);
	}

	public static boolean hasTokenExpired(String value) {
		
		boolean returnValue = false;
		
		try {
			SecretKey secretKey = new SecretKeySpec(
					Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes()),
					SignatureAlgorithm.HS512.getJcaName());
			
			Claims claims = (Claims) Jwts.parser().setSigningKey(secretKey).build()
					.parseClaimsJws(value).getPayload();
	
			Date tokenExpirationDate = claims.getExpiration();
			Date todayDate = new Date();
			
			returnValue = tokenExpirationDate.before(todayDate);
		}
		catch (ExpiredJwtException ex) {
			returnValue = true;
		}
		
		return returnValue;
	}

	public static String generateEmailVerificationToken(String userId) {

		SecretKey secretKey = new SecretKeySpec(
				Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes()),
				SignatureAlgorithm.HS512.getJcaName());

		String token = Jwts.builder().setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS512).compact();

		return token;
	}
	
	public static String generatePasswordResetToken(String userId) {

		SecretKey secretKey = new SecretKeySpec(
				Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes()),
				SignatureAlgorithm.HS512.getJcaName());

		String token = Jwts.builder().setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS512).compact();

		return token;
	}
}
