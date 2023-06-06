package com.bank.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

		@Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문과 숫자를 조합해서 2~20자 이내로 작성해주세요.") // 2-1. 영문, 숫자는 되고, 길이는 최소 2~20자 이내
		@NotEmpty  // 1-1. null이거나 공백일 수 없다.
		private String username;  
		
		@Size(min = 4, max = 20)
		@NotEmpty
		private String password;  // 길이 4~20
		
		@Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해 주세요.")
		@NotEmpty
		private String email;  // 이메일 형식
		
		@Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글과 영문으로 1~20자 이내로 작성해주세요.")
		@NotEmpty
		private String fullname;  // 영어, 한글, 길이는 최소 1~20
		
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
