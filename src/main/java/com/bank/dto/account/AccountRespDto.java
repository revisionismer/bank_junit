package com.bank.dto.account;

import com.bank.domain.account.Account;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountRespDto {

	private Long id;
	private Long number;
	private Long balance;
	
	public AccountRespDto(Account account) {
		this.id = account.getId();
		this.number = account.getNumber();
		this.balance = account.getBalance();
	}
}
