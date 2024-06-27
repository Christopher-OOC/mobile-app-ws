package com.appdeveloperblog.app.io.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 5111707505377091226L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	private String userId;
	
	@Column(nullable=false, length=50)
	private String firstName;
	
	@Column(nullable=false, length=50)
	private String lastName;
	
	@Column(nullable=false, length=120, unique=true)
	private String email;
		
	@Column(nullable=false)
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	@Column(nullable=false)
	private Boolean emailVerificationStatus = false;
	
	@OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<AddressEntity> addresses;
	
	@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinTable(name="users_roles", joinColumns=@JoinColumn(name="users_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="roles_id", referencedColumnName="id"))
	private Collection<RoleEntity> roles;

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", encryptedPassword=" + encryptedPassword + ", emailVerificationToken="
				+ emailVerificationToken + ", emailVerificationStatus=" + emailVerificationStatus + ", addresses="
				+ addresses + "]";
	}
}
