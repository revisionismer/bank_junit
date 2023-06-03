package com.bank.dto.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.constant.user.UserEnum;
import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinReqDto {

	private String username;
	private String password;
	private String email;
	private String fullname;
	
	public User toEntity(BCryptPasswordEncoder passwordEncoder) {
		return User.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.email(email)
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.build();
	}
}
