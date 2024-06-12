package com.appdeveloperblog.app.ui.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressRequestModel {
	
	private String city;
	
	private String country;
	
	private String streetName;
	
	private String postalCode;
	
	private String type;

}
