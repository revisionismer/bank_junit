package com.bank.dto.account;

import com.bank.domain.account.Account;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountDto {

	private Long id;
	private String number;
	private Long balance;
	
	public AccountDto(Account account) {
		this.id = account.getId();
		this.number = account.getNumber();
		this.balance = account.getBalance();
	}
}
