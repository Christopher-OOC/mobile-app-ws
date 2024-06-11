package com.appdeveloperblog.app.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appdeveloperblog.app.UserRepository;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.shared.Utils;
import com.appdeveloperblog.app.shared.dto.UserDto;

@Service
public class UserServiceImpl  implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Utils utils;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl() {
		
	}

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Record already exists");
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String publicUserId = utils.generateUserId(30);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException("No user found with email: " + email);
		}
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}
}
