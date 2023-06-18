package com.bank.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bank.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

	@Profile("dev")
	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return (args) -> {
			// 서버 실행시에 무조건 실행된다.
			userRepository.save(newUser("ssar", "1234", "ssar@nate.com", "쌀"));
		};
	}
}
