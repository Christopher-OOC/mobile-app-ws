package com.appdeveloperblog.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.appdeveloperblog.app.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

}
