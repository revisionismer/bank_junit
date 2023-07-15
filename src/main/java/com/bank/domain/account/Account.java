package com.bank.domain.account;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bank.domain.user.User;
import com.bank.handler.exception.CustomApiException;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class) 
@NoArgsConstructor
@Getter
@Table(name = "account_tb")
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 2-1. pk
	
	@Column(unique = true, nullable = false, length = 20)
	private String number;  // 2-2. 계좌번호
	
	@Column(nullable = false, length = 100)
	private String password;  // 2-3. 계좌 비밀번호
	
	@Column(nullable = false)
	private Long balance;  // 2-4. 잔액(기본 값 1000원)
	
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;  // 2-5. 생성일
	 
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;   // 2-6. 수정일
	
	// 참고 : 항상 ORM에서 FK의ㅣ 주인은 Many 쪽이다.
	@ManyToOne(fetch = FetchType.LAZY)  // 2-8. 지연 로딩 설정(user의 멤버변수를 접근하는 시점에 가져온다.)
	private User user;  // 2-7. User Entity와 연관관계 설정(N : 1)
	
	// 2-9. builder 패턴 생성
	@Builder
	public Account(Long id, String number, String password, Long balance, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.number = number;
		this.password = password;
		this.balance = balance;
		this.user = user;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	// 2-10. 계좌 소유주와 비밀번호 확인 메서드
	public void checkOwner(Long userId) {
		if(user.getId() != userId) {
			throw new CustomApiException("소유자가 다릅니다.");
		} 
	}
	
	// 3-1. 입금하기
	public void deposit(Long amount) {
		balance = balance + amount;
	}
	
	// 4-1. 계좌 비밀번호 확인
	public void checkSamePassword(String rawPassword, PasswordEncoder passwordEncoder) {
		if(!passwordEncoder.matches(rawPassword, password)) {
			throw new CustomApiException("계좌 비밀번호가 다릅니다.");
		}
	}
	// 5-1. 잔액 확인
	public void checkBalance(Long amount) {
		if(this.balance < amount) {
			throw new CustomApiException("계좌 잔액이 부족합니다.");
		}
	}
	
	// 6-1. 출금
	public void withdraw(Long amount) {
		// 6-2. 보험으로 잔액확인 로직 넣어둠.
		checkBalance(amount);
		balance = balance - amount;
	}
}
