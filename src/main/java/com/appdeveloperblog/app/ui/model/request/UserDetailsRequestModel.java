package com.appdeveloperblog.app.ui.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailsRequestModel {
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	

}
