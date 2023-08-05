package com.bank.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.constant.transaction.TransactionEnum;
import com.bank.constant.user.UserEnum;
import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.user.User;

public class DummyObject {
	// 1-1. 사용자 생성
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
	
	// 1-2. 계좌 생성
	protected Account newAccount(String number, Long balance, User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("1234");
		
		return Account.builder()
				.number(number)
				.password(encPassword)
				.balance(balance)
				.user(user)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
	}
	
	// 1-3. 입금거래 내역
	protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
		account.deposit(100L);
		// 2-1. Repository Test에서는 더티체킹이 되기 때문에 아래 코드를 주석처리 해줘도 더티체킹이 동작한다.
		// 2-2. 그러나 Contoller Test에서는 더티체킹이 안된다. 그렇기 때문에 아래 코드를 넣어줘야 한다.
		if(accountRepository != null) {
			accountRepository.save(account);
		}
	
		return Transaction.builder()  
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
	
	// 1-4. 출금거래내역
	protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
		account.withdraw(100L);
		
		if(accountRepository != null) {
			accountRepository.save(account);
		}

		return Transaction.builder()  
				.depositAccount(null)
				.depositAccountBalance(null)
				.withdrawAccount(account)
				.withdrawAccountBalance(account.getBalance())
				.amount(100L)
				.gubun(TransactionEnum.WITHDRAW)
				.sender(account.getNumber())
				.receiver("ATM")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
	}
	
	// 1-5. 이체거래내역
	protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount, AccountRepository accountRepository) {
		withdrawAccount.withdraw(100L);
		depositAccount.deposit(100L);
		
		if(accountRepository != null) {
			accountRepository.save(withdrawAccount);
			accountRepository.save(depositAccount);
		}

		return Transaction.builder()  
				.depositAccount(depositAccount)
				.depositAccountBalance(depositAccount.getBalance())
				.withdrawAccount(withdrawAccount)
				.withdrawAccountBalance(withdrawAccount.getBalance())
				.amount(100L)
				.gubun(TransactionEnum.TRANSFER)
				.sender(withdrawAccount.getNumber())
				.receiver(depositAccount.getNumber())
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
	}	
	
	// 1-6. Mock 사용자
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
	
	// 1-7. Mock 계좌
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
	
	// 1-8. Mock Transaction
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
