package com.appdeveloperblog.app.service;

import java.util.List;

import com.appdeveloperblog.app.shared.dto.AddressDto;

public interface AddressService {
	
	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);
}
