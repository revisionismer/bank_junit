package com.bank.dto.transaction;

import java.time.LocalDateTime;

import com.bank.domain.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionWithdrawDto {

	private Long id;
	
	private String gubun;
	
	private String sender;
	
	private String receiver;
	
	private Long amount;
	
	@JsonIgnore
	private Long withdrawAccountBalance;  
	
	private LocalDateTime createdAt;

	public TransactionWithdrawDto(Transaction transaction) {
		this.id = transaction.getId();
		this.gubun = transaction.getGubun().getValue();
		this.sender = transaction.getSender();
		this.receiver = transaction.getReceiver();
		this.amount = transaction.getAmount();
		this.withdrawAccountBalance = transaction.getWithdrawAccountBalance();
		this.createdAt = transaction.getCreatedAt();
	}
	
	
}
