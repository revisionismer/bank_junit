package com.bank.dto.transaction;

import java.time.LocalDateTime;

import com.bank.domain.transaction.Transaction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionDto {
	
	private Long id;
	private String gubun;
	private Long amount;
	private String sender;
	private String receiver;
	private String tel;
	private LocalDateTime createdAt;
	private Long balance;
	
	public TransactionDto(Transaction transaction, String accountNumber) {
		this.id = transaction.getId();
		this.gubun = transaction.getGubun().getValue();
		this.amount = transaction.getAmount();
		this.sender = transaction.getSender();
		this.receiver = transaction.getReceiver();
		this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();
		this.createdAt = transaction.getCreatedAt();
		
		if(transaction.getDepositAccount() == null) {
			this.balance = transaction.getWithdrawAccountBalance();
		} else if(transaction.getWithdrawAccount() == null) {
			this.balance = transaction.getDepositAccountBalance();
		} else {
			// 1-1. 조회하려는 계좌가 입금계좌라면 입금계좌의 잔액을 보여줘야 한다.
			if(accountNumber.equals(transaction.getDepositAccount().getNumber())) {
				this.balance = transaction.getDepositAccountBalance();
			} else {
				this.balance = transaction.getWithdrawAccountBalance();
			}
		}
		
	}
	
}