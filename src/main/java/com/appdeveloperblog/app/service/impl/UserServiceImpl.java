package com.appdeveloperblog.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appdeveloperblog.app.UserRepository;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.shared.Utils;
import com.appdeveloperblog.app.shared.dto.UserDto;
import com.appdeveloperblog.app.ui.model.response.ErrorMessages;
import com.appdeveloperblog.app.ws.exceptions.UserServiceException;

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

	@Override
	public UserEntity findByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		return userEntity;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId("No user with ID: " + userId);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException("No user found with email: " + userId);
		}
		
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDto updateUser(String id, UserDto userDto) {
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(id);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
	
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		
		if (page > 0) {
			page = page - 1;
		}
		
		Pageable pageable = PageRequest.of(page, limit);
		
		Page<UserEntity> pageList = userRepository.findAll(pageable);
		
		List<UserEntity> users = pageList.getContent();
		
		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		
		return returnValue;
	}
}
