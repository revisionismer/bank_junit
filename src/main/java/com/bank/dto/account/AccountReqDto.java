package com.bank.dto.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountReqDto {
	
	@NotNull
	@Size(min = 4, max = 6, message = "계좌번호는 4자리에서 6자리로 입력해주세요.") // 1-1. input number 타입은 @Min, @Max로 validation
	private String number;
	
	@NotNull
	@Size(min = 4, max = 4, message = "계좌 비밀번호는 4자리로 입력해주세요.")
	private String password;  // 1-2. 
	
	public Account toEntity(User user, PasswordEncoder passwordEncoder) {
		return Account.builder()
				.number(number)
				.password(passwordEncoder.encode(password))
				.balance(1000L)
				.user(user)
				.build();
	}
}
