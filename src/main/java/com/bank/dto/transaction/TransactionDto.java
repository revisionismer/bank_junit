package com.bank.dto.transaction;

import java.time.LocalDateTime;

import com.bank.domain.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionDto {

	private Long id;
	
	private String gubun;
	
	private String sender;
	
	private String receiver;
	
	private Long amount;
	
	@JsonIgnore
	private Long depositAccountBalance;  
	
	private String tel;
	
	private LocalDateTime createdAt;

	public TransactionDto(Transaction transaction) {
		this.id = transaction.getId();
		this.gubun = transaction.getGubun().getValue();
		this.sender = transaction.getSender();
		this.receiver = transaction.getReceiver();
		this.amount = transaction.getAmount();
		this.depositAccountBalance = transaction.getDepositAccountBalance();
		this.tel = transaction.getTel();
		this.createdAt = transaction.getCreatedAt();
	}
	
	
}
