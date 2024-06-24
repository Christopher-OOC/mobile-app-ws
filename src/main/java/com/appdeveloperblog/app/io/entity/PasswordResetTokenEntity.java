package com.appdeveloperblog.app.io.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="password_reset_tokens")
public class PasswordResetTokenEntity implements Serializable {
	 
	private static final long serialVersionUID = 1494117718905718452L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String token;
	
	@OneToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;
}
