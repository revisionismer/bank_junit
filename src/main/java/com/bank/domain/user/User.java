package com.bank.domain.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // 1-2. JPA에서 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문에 추가.
@Getter @Setter
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 1-1. 기본키는 DB에 맡게 설정.
	private Long id;
}
