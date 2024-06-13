package com.appdeveloperblog.app.io.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="addresses")
public class AddressEntity implements Serializable {

	
	private static final long serialVersionUID = -7924937063341083065L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(length=30, nullable=false)
	private String addressId;
	
	@Column(length=15, nullable=false)
	private String city;
	
	@Column(length=15, nullable=false)
	private String country;
	
	@Column(length=100, nullable=false)
	private String streetName;
	
	@Column(length=7, nullable=false)
	private String postalCode;
	
	@Column(length=10, nullable=false)
	private String type;
	
	@ManyToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;

	@Override
	public String toString() {
		return "AddressEntity [id=" + id + ", addressId=" + addressId + ", city=" + city + ", country=" + country
				+ ", streetName=" + streetName + ", postalCode=" + postalCode + ", type=" + type + "]";
	}
	
	
	
}
