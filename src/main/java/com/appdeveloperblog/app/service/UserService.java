package com.appdeveloperblog.app.service;

import java.util.List;

import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.shared.dto.UserDto;

public interface UserService {
	
	UserDto createUser(UserDto user);
	
	UserEntity findByEmail(String email);
	
	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userId);

	UserDto updateUser(String id, UserDto userDto);

	void deleteUser(String userId);

	List<UserDto> getUsers(int page, int limit);

	boolean verifyEmailToken(String token);

}
