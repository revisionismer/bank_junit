package com.bank.service.account;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.account.AccountReqDto;
import com.bank.dto.account.AccountRespDto;
import com.bank.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	
	public AccountRespDto createAccount(AccountReqDto accountReqDto, String username) {
		// 1-1. User가 DB에 있는지 검증
		Optional<User> userOp = userRepository.findByUsername(username);
		
		if(userOp.isPresent()) {   // 1-2. 해당 유저가 DB에 있는 유저면 계좌 생성 진행
			
			User user = userOp.get();  // 1-3. 유저를 get해온다.
			
			// 1-4. 요청 계좌 정보가 DB에 있는지 검증 하기위해 Optional로 가져온다.
			Optional<Account> accountOp = accountRepository.findByNumber(accountReqDto.getNumber());
			
			if(accountOp.isPresent()) {  // 1-5. 만들어 줄 계좌가 이미 존재하면
				// 1-6. 익셉션을 터트려준다.
				throw new CustomApiException("존재하는 계좌입니다.");
			} else {
				// 1-7. 존재하지 않는 계좌면 해당 유저 정보로 신규 계좌 생성
				Account accountPS = accountRepository.save(accountReqDto.toEntity(user));
				
				// 1-5. DTO로 응답
				return new AccountRespDto(accountPS);
			}
			
		} else {
			throw new CustomApiException("존재하지 않는 회원입니다.");
		}
	}
	
	
}
