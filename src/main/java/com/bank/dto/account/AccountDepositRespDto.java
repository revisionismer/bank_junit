package com.bank.dto.account;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.dto.transaction.TransactionDepositDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountDepositRespDto {

	private Long id;  // 1-1. 계좌 id
	private String number;  // 1-2. 계좌 번호
	private TransactionDepositDto  transaction;  // 1-3. 거래내역 
	
	public AccountDepositRespDto(Account account, Transaction transaction) {
		this.id = account.getId();
		this.number = account.getNumber();
		this.transaction = new TransactionDepositDto (transaction);
	}
}
