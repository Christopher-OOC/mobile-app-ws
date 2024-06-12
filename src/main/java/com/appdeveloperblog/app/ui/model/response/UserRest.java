package com.appdeveloperblog.app.ui.model.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRest {
	private String userId;

	private String firstName;

	private String lastName;

	private String email;
	
	private List<AddressRest> addresses;

}
