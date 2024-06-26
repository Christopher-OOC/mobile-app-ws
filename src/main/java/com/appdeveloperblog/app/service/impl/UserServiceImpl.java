package com.appdeveloperblog.app.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appdeveloperblog.app.repository.PasswordResetTokenRepository;
import com.appdeveloperblog.app.repository.RoleRepository;
import com.appdeveloperblog.app.repository.UserRepository;
import com.appdeveloperblog.app.io.entity.PasswordResetTokenEntity;
import com.appdeveloperblog.app.io.entity.RoleEntity;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.shared.AmazonSES;
import com.appdeveloperblog.app.shared.Utils;
import com.appdeveloperblog.app.shared.dto.AddressDto;
import com.appdeveloperblog.app.shared.dto.UserDto;
import com.appdeveloperblog.app.ui.model.response.ErrorMessages;
import com.appdeveloperblog.app.ws.exceptions.UserServiceException;

@Service
public class UserServiceImpl  implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	

	@Autowired
	private Utils utils;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl() {
		
	}

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new UserServiceException("Record already exists");
		}
		
		for (int i = 0; i < user.getAddresses().size(); i++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}

		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		String publicUserId = utils.generateUserId(30);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		userEntity.setEmailVerificationToken(Utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(false);
		
		Collection<RoleEntity> roleEntities = new HashSet<>();
		
		for (String role : user.getRoles()) {
			RoleEntity roleEntity = roleRepository.findByName(role);
			
			if (roleEntity != null) {
				roleEntities.add(roleEntity);
			}
		}
		
		userEntity.setRoles(roleEntities);
		
		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
		
		new AmazonSES().verifyEmail(returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException("No user found with ID: " + userId);
		}
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}

	@Override
	public UserEntity findByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		return userEntity;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException("No user found with Id: " + userId);
		}
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		
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
			ModelMapper modelMapper = new ModelMapper();
			UserDto userDto = modelMapper.map(userEntity, UserDto.class);
			returnValue.add(userDto);
		}
		
		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;
		
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
		
		if (userEntity != null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			
			if (!hasTokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(true);
				
				userRepository.save(userEntity);
				
				returnValue = true;
			}
		}
		
		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		
		boolean returnValue = false;
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			return returnValue;
		}
		
		String token = Utils.generatePasswordResetToken(userEntity.getUserId());
		
		PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
		passwordResetTokenEntity.setToken(token);
		passwordResetTokenEntity.setUserDetails(userEntity);
		
		passwordResetTokenRepository.save(passwordResetTokenEntity);
		
		returnValue = new AmazonSES().sendPasswordResetRequest(
				userEntity.getFirstName(),
				userEntity.getEmail(),
				token
				);
				
		
		return returnValue;
	}
}
