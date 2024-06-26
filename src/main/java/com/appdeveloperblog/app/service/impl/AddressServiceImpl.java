package com.appdeveloperblog.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdeveloperblog.app.io.entity.AddressEntity;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.repository.AddressRepository;
import com.appdeveloperblog.app.repository.UserRepository;
import com.appdeveloperblog.app.service.AddressService;
import com.appdeveloperblog.app.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		
		ModelMapper modelMapper = new ModelMapper();
		
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			return returnValue;
		}
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}

		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto returnValue = null;
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
	
		if (addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}

}
