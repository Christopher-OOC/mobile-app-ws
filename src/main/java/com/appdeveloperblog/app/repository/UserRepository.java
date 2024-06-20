package com.appdeveloperblog.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.appdeveloperblog.app.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {

	@Query("SELECT u FROM UserEntity u WHERE u.email = ?1")
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token);

	@Query(value = "select * from users u where u.email_verification_status = 'true'", countQuery = "select count(*) from users u where u.email_verification_status = 'true'", nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddreess(Pageable pageableRequest);

	@Query(value = "select * from users u where u.first_name = ?1 and u.last_name = ?2", nativeQuery = true)
	List<UserEntity> findUUserByFirstName(String firstName, String lastName);

	@Query(value = "select * from users u where u.first_name = :firstName and u.last_name = :lastName", nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

	@Query(value = "select * from users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%", nativeQuery = true)
	List<UserEntity> findUsersByKeyWord(@Param("keyword") String keyword);

}
