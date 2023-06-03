package com.bank.service.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.user.JoinReqDto;
import com.bank.dto.user.JoinRespDto;
import com.bank.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public JoinRespDto join(JoinReqDto joinReqDto) {
		log.info("회원가입");
		// 1-1. 동일 회원 아이디가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(joinReqDto.getUsername());
		
		if(userOp.isPresent()) {
			throw new CustomApiException("중복된 아이디입니다.");
		}
		
		// 1-2. 패스워드 인코딩 + 회원가입
		User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));
		
		// 1-3. dto 응답
		return new JoinRespDto(userPS);
	}
	
	
}
