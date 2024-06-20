package com.appdeveloperblog.app.repository;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.appdeveloperblog.app.io.entity.AddressEntity;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(true)
public class UserRepositoryTests {
	
	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Christopher");
		userEntity.setLastName("Olojede");
		userEntity.setUserId("1jj1h332");
		userEntity.setEncryptedPassword("tydthsd");
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("hsgdhgsd");
		addressEntity.setCity("Vancouver");
		addressEntity.setCountry("ABCCDA");
		addressEntity.setPostalCode("dghhdhfd");
		addressEntity.setStreetName("123 kjds");
		
		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);
		
		userEntity.setAddresses(addresses);
		
		userRepository.save(userEntity);
	}

	@Test
	void testGetVerifiiedUsers() {
		Pageable pageableRequest = PageRequest.of(0, 2);
		
		Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddreess(pageableRequest);
		
		assertNotNull(pages);
		
		List<UserEntity> userEntities = pages.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
	}

}
