package com.appdeveloperblog.app.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appdeveloperblog.app.io.entity.AddressEntity;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.repository.UserRepository;
import com.appdeveloperblog.app.shared.Utils;
import com.appdeveloperblog.app.shared.dto.AddressDto;
import com.appdeveloperblog.app.shared.dto.UserDto;
import com.appdeveloperblog.app.ws.exceptions.UserServiceException;

@SpringBootTest
class UserServiceImplTests {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	String userId = "iusyudsdsjdilfkl";
	
	String password = "kidjhdjshdosid";
	
	UserEntity userEntity;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		userEntity = new UserEntity();
		
		userEntity.setId(1L);
		userEntity.setFirstName("Christopher");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(password);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("skjhhdgfdgdjd");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	final void testGetUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
	
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Christopher", userDto.getFirstName());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
	}
	
	@Test
	final void testCreateUser_throwsUserServiceException() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Sergey");
		userDto.setLastName("Olojede");
		userDto.setPassword("123456");
		userDto.setEmail("test@test.com");
		
		assertThrows(UserServiceException.class, () -> {
			userService.createUser(userDto);
		});
	}
	
	@Test
	final void testCreateUser() {
				
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("jkjshdisudsjs");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(password);
		
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		//Mockito.doNothing().when(null)
		
		List<AddressDto> addresses = getAddressesDto();

		UserDto userDto = new UserDto();
		userDto.setAddresses(addresses);
		userDto.setFirstName("Sergey");
		userDto.setLastName("Olojede");
		userDto.setPassword("123456");
		userDto.setEmail("test@test.com");
		
		UserDto storedUserDetails = userService.createUser(userDto);
		
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils, times(2)).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("123456");
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	private List<AddressDto> getAddressesDto() {
		AddressDto addressDto = new AddressDto();
		addressDto.setType("Shipping");
		addressDto.setCity("Vancouver");
		addressDto.setCountry("Canada");
		addressDto.setPostalCode("ABCCBA");
		addressDto.setStreetName("123 Street Name");
		
		AddressDto billingAddressDto = new AddressDto();
		addressDto.setType("Shipping");
		addressDto.setCity("Vancouver");
		addressDto.setCountry("Canada");
		addressDto.setPostalCode("ABCCBA");
		addressDto.setStreetName("123 Street Name");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntity() {
		List<AddressDto> addresses = getAddressesDto();
		
		java.lang.reflect.Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		
		return new ModelMapper().map(addresses, listType);
	}

}
