package com.appdeveloperblog.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appdeveloperblog.app.io.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
	RoleEntity findByName(String name);

}
