package com.bank.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	// 1-1. JPA namedQuery 1
	Optional<User> findByUsername(String username);
}
