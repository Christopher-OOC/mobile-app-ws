package com.appdeveloperblog.app.ui.model.response;

import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressRest extends RepresentationModel<AddressRest> {

	private String addressId;
	
	private String city;
	
	private String country;
	
	private String streetName;
	
	private String postalCode;
	
	private String type;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressRest other = (AddressRest) obj;
		return Objects.equals(addressId, other.addressId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(addressId);
		return result;
	}
	
	

}
