package com.appdeveloperblog.app.ui.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginRequestModel {
	
	private String email;
	
	private String password;

}
