package com.appdeveloperblog.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appdeveloperblog.app.shared.dto.UserDto;

public interface UserService  extends UserDetailsService {
	
	UserDto createUser(UserDto user);

}
