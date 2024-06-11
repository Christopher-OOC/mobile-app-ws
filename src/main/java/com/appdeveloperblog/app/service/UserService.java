package com.appdeveloperblog.app.service;

import com.appdeveloperblog.app.shared.dto.UserDto;

public interface UserService {
	
	UserDto createUser(UserDto user);
	
	UserDto getUser(String email);

}
