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

import com.bank.domain.user.User;

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
	private Long number;  // 2-2. 계좌번호
	
	@Column(nullable = false, length = 4)
	private Long password;  // 2-3. 계좌 비밀번호
	
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
	public Account(Long id, Long number, Long password, Long balance, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.number = number;
		this.password = password;
		this.balance = balance;
		this.user = user;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
