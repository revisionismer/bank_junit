package com.bank.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{

	// jpa query method
	// select * from account where number = :number
	// checkpoint : 리팩토링 대상
	Optional<Account> findByNumber(Long number);
}
