package com.bank.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.constant.transaction.TransactionEnum;
import com.bank.constant.user.UserEnum;
import com.bank.domain.account.Account;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.user.User;

public class DummyObject {
	// 1-1. 진짜
	protected User newUser(String username, String password, String email, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode(password);
		
		return User.builder()
				.username(username)
				.password(encPassword)
				.email(email)
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.build();
	}
	
	// 1-2. 가짜
	protected User newMockUser(Long id, String username, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("1234");
		
		return User.builder()
				.id(id)
				.username(username)
				.password(encPassword)
				.email(username + "@nate.com")
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.createdAt(LocalDateTime.now())
				.updateAt(LocalDateTime.now())
				.build();
	}
	
	// 1-3. 가짜 계좌
	protected Account newMockAccount(Long id, String number, Long balance, User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("1234");
		
		return Account.builder()
				.id(id)
				.number(number)
				.password(encPassword)
				.balance(balance)
				.user(user)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
	}
	
	// 1-4. 가짜 Transaction
	protected Transaction newMockDepositTransaction(Long id, Account account) {
		
		// 1-6. 거래내역 생성
		return Transaction.builder()  
				.id(id)
				.depositAccount(account)
				.depositAccountBalance(account.getBalance())
				.withdrawAccount(null)
				.withdrawAccountBalance(null)
				.amount(100L)
				.gubun(TransactionEnum.DEPOSIT)
				.sender("ATM")
				.receiver(account.getNumber())
				.tel("010-1234-5678")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
	}
}
