package com.bank.dto.account;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountWithdrawReqDto {
	
	@NotNull
	@Size(min = 4, max = 6, message = "계좌번호는 4자리에서 6자리로 입력해주세요.")
	private String number;
	
	@NotNull
	@Size(min = 4, max = 4, message = "계좌 비밀번호는 4자리로 입력해주세요.")
	private String password; 
	
	@NotNull
	@Positive(message = "0보다 큰 수를 입력해주세요.")
	private Long amount;
	
	@NotEmpty
	@Pattern(regexp = "^(WITHDRAW)$")  // 1-1. "^(WITHDRAW)$ == WITHDRAW
	private String gubun;  // WITHDRAW
}
