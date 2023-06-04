package com.bank.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.constant.user.UserEnum;
import com.bank.domain.user.User;

public class DummyObject {
	// 1-1. 진짜
	protected User newUser(String username, String password, String email, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode(password);
		
		return User.builder()
				.username(username)
				.password(encPassword)
				.email(email)
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.build();
	}
	
	// 1-2. 가짜
	protected User newMockUser(Long id, String username, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("1234");
		
		return User.builder()
				.id(id)
				.username(username)
				.password(encPassword)
				.email(username + "@nate.com")
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.createdAt(LocalDateTime.now())
				.updateAt(LocalDateTime.now())
				.build();
	}
}
