package com.appdeveloperblog.app;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appdeveloperblog.app.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	@Query("SELECT u FROM UserEntity u WHERE u.email = ?1")
	UserEntity findByEmail(String email);
	
	UserEntity findByUserId(String userId);
}
