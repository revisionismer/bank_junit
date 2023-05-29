package com.bank.domain.transaction;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bank.constant.transaction.TransactionEnum;
import com.bank.domain.account.Account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class) 
@NoArgsConstructor
@Getter
@Table(name = "transaction_tb")
@Entity
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 3-1. pk
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account withdrawAccount;  // 3-2. 
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account depositAccount;  // 3-3.
	
	@Column(nullable = false)
	private Long amount;  // 3-4
	
	private Long withdrawAccountBalance;  // 3-5. 출금 history 
	
	private Long depositAccountBalance;  // 3-6. 예금 history
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionEnum gubun;  // 3-7. 구분 : WITHDRAW(출금), DEPOSIT(예금), TRANSFER(이체), ALL

	// 계좌가 사라져도 로그는 남겨야 하기에 
	private String sender;  // 3-8. 송신자 정보.
	private String receiver;  // 3-9. 수신자 정보.
	private String tel; // 3-10. 전화번호
	
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;  // 3-11. 생성일
	 
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;   // 3-12. 수정일

	@Builder  // 3-13. Builder 패턴 생성
	public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount,
			Long withdrawAccountBalance, Long depositAccountBalance, TransactionEnum gubun, String sender,
			String receiver, String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
		
		this.id = id;
		this.withdrawAccount = withdrawAccount;
		this.depositAccount = depositAccount;
		this.amount = amount;
		this.withdrawAccountBalance = withdrawAccountBalance;
		this.depositAccountBalance = depositAccountBalance;
		this.gubun = gubun;
		this.sender = sender;
		this.receiver = receiver;
		this.tel = tel;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
}
