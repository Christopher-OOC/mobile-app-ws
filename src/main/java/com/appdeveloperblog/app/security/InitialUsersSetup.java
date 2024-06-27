package com.appdeveloperblog.app.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.appdeveloperblog.app.io.entity.AuthorityEntity;
import com.appdeveloperblog.app.io.entity.AuthorityRepository;
import com.appdeveloperblog.app.io.entity.RoleEntity;
import com.appdeveloperblog.app.io.entity.UserEntity;
import com.appdeveloperblog.app.repository.RoleRepository;
import com.appdeveloperblog.app.repository.UserRepository;
import com.appdeveloperblog.app.shared.Roles;
import com.appdeveloperblog.app.shared.Utils;

@Component
public class InitialUsersSetup {
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private static String INITIAL_ADMIN_EMAIL = "olojedechristopher24@gmail.com";
	
	@EventListener 
	@Transactional
	 public void onApplicationEvent(ApplicationReadyEvent event) {
		 System.out.println("This is Christopher");
		 
		 AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		 AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		 AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");	 
		 
		 createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
		 RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
		 
		 UserEntity admin = userRepository.findByEmail(INITIAL_ADMIN_EMAIL);
		 
		 if (admin == null) {
			 
			 UserEntity adminUser = new UserEntity();
			 adminUser.setFirstName("Christopher");
			 adminUser.setLastName("Olojede");
			 adminUser.setEmail(INITIAL_ADMIN_EMAIL);
			 adminUser.setEmailVerificationStatus(true);
			 adminUser.setUserId(utils.generateUserId(30));
			 adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("christopher"));
			 adminUser.setRoles(Arrays.asList(roleAdmin));
			 
			 userRepository.save(adminUser);
		 }
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {
		
		AuthorityEntity authority = authorityRepository.findByName(name);
		
		if (authority == null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		
		return authority;
	}
	
	@Transactional
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		RoleEntity role = roleRepository.findByName(name);
		
		if (role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			
			roleRepository.save(role);
		}
		
		return role;
	}

}
