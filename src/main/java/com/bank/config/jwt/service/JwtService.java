package com.bank.config.jwt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.jwt.JwtProperties;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.handler.exception.CustomApiException;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Data
@Slf4j
public class JwtService {
	
	private final UserRepository userRepository;

	private static String secretKey = JwtProperties.SECRET_KEY;
	
	byte[] secretKeyBytes = secretKey.getBytes();
	
	public void setRefreshToken(String username, String refreshToken) {
		
		log.info("refresh token 로그인 유저에 셋팅");
		
		Optional<User> userOp = userRepository.findByUsername(username);
		
		if(userOp.isPresent()) {
			
			User findUser = userOp.get();
			findUser.setRefreshToken(refreshToken);
			
			userRepository.save(findUser);
			
		} else {
			throw new CustomApiException("Refresh Token update error");
		}
	}
}
