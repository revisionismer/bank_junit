package com.bank.dto.account;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountDepositReqDto {

	@NotNull
	@Size(min = 4, max = 6, message = "계좌번호는 4자리에서 6자리로 입력해주세요.")
	private String number;
	
	@NotNull
	private Long amount;
	
	@NotEmpty
	@Pattern(regexp = "^(DEPOSIT)$")  // 1-1. "^(DEPOSIT)$ == DEPOSIT
	private String gubun;  // DEPOSIT
	
	@NotEmpty
	@Pattern(regexp = "^[0-9]{11}")  // 1-2. ^[0-9]{3}[0-9]{4}[0-9]{4} == ^[0-9]{11}
	private String tel;
}
