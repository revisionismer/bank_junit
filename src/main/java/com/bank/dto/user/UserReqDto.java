package com.bank.dto.user;

import javax.validation.constraints.NotEmpty;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.constant.user.UserEnum;
import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;

// Static 변수가 붙은 변수나 클래스는 클래스가 메모리에 올라갈 때 자동으로 생성이 됩니다.
// 상위 클래스에 포함된 하위 클래스는 미리 생성이 되어야 사용이 가능합니다.
public class UserReqDto {

	@Getter @Setter
	public static class JoinReqDto {

		@NotEmpty  // 1-1. null이거나 공백일 수 없다.
		private String username;
		
		@NotEmpty
		private String password;
		
		@NotEmpty
		private String email;
		
		@NotEmpty
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
}
