package com.bank.dto.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.dto.transaction.TransactionDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountDetailRespDto {
	
	private Long id;  // 1-1. 계좌 PK 
	private String number;  // 1-2. 계좌 번호
	private Long balance;  // 1-3. 계좌 잔액
	private List<TransactionDto> transactions = new ArrayList<>();  // 1-4. 계좌 입출금 내역

	public AccountDetailRespDto(Account account, List<Transaction> transactions) {
		this.id = account.getId();
		this.number = account.getNumber();
		this.balance = account.getBalance();
		this.transactions = transactions.stream().map(
												(transaction) -> new TransactionDto(transaction, account.getNumber())
											).collect(Collectors.toList());
	}
}
