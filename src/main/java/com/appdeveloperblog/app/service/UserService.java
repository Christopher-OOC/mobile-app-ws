package com.appdeveloperblog.app.service;

import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.shared.dto.UserDto;

public interface UserService {
	
	UserDto createUser(UserDto user);
	
	UserEntity findByEmail(String email);
	
	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userId);

}
