package com.bank.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{

	// jpa query method
	// select * from account where number = :number
	// checkpoint : 리팩토링 대상(계좌 소유자  확인 작업시 쿼리가 두번 나가기 때문, join fetch)
	Optional<Account> findByNumber(String number);
}
