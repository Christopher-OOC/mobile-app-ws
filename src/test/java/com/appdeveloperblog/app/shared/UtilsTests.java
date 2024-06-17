package com.appdeveloperblog.app.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilsTests {
	
	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userId = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
	
		assertNotNull(userId);
		assertNotNull(userId2);
		assertTrue(userId.length() == 30);
		assertTrue(userId2.length() == 30);
		assertTrue(!userId.equalsIgnoreCase(userId2));
	}

	@Test
	void testHasTokenExpired() {
		String token = Utils.generateEmailVerificationToken("jhdcdf");
		assertNotNull(token);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		
		assertFalse(hasTokenExpired);
	
	}
	
	@Test
	void testHasTokenNotExpired() {
		
		// 	This will expired later  
		
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJQbVh3RXFUMlZPN2hpNzV0bUVSeG96aGxEbml4UjciLCJleHAiOjE3MTk1MDIyNjB9.7Ff5UI-mQYdNEZlJ6RREBri389I8p5oFjHQMZTfEL9KOlaGLErQauXF6XxHQ50Y7-orFQw2kcw9GDQ13BlzQMg";
	
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		
		assertFalse(hasTokenExpired);
	
	}

}
