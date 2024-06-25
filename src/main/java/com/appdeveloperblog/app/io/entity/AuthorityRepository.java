package com.appdeveloperblog.app.io.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
	
	AuthorityEntity findByName(String name);

}
