package com.bank.dto.account;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.dto.transaction.TransactionDepositDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountTransferRespDto {
	private Long id;  // 1-1. 계좌 id
	private String number;  // 1-2. 계좌 번호
	private Long balance;  // 1-3. 출금계좌 잔액
	private TransactionDepositDto transaction;  // 1-4. 거래내역 
	
	public AccountTransferRespDto(Account account, Transaction transaction) {
		this.id = account.getId();
		this.number = account.getNumber();
		this.balance = account.getBalance();
		this.transaction = new TransactionDepositDto(transaction);
	}
}
