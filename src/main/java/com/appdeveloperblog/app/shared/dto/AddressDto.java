package com.appdeveloperblog.app.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
	
	private long id;
	
	private String addressId;
	
	private String city;
	
	private String country;
	
	private String streetName;
	
	private String postalCode;
	
	private String type;
	
	private UserDto userDetails;

}
