package com.appdeveloperblog.app.shared.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String userId;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	private Boolean emailVerificationStatus = false;
	
	private List<AddressDto> addresses;
	

}
