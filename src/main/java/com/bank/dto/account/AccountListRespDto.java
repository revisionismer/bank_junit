package com.bank.dto.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.domain.account.Account;
import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountListRespDto {

	private String fullname;
	private List<AccountDto> accounts = new ArrayList<>();
	
	public AccountListRespDto(User user, List<Account> accounts) {
		this.fullname = user.getFullname();
		this.accounts = accounts.stream().map( (account) -> new AccountDto(account) ).collect(Collectors.toList());
	}
}
