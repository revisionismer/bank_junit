package com.bank.dto.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountReqDto {
	
	@NotNull
	@Digits(integer = 10, fraction = 10)  // 1-1. 숫자 길이는 @Size가 아니라 @Digits로 validation
	private Long number;
	
	@NotNull
	@Digits(integer = 4, fraction = 4)
	private Long password;
	
	public Account toEntity(User user) {
		return Account.builder()
				.number(number)
				.password(password)
				.balance(1000L)
				.user(user)
				.build();
	}
}
