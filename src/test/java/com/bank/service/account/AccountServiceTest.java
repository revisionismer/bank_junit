package com.bank.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.config.dummy.DummyObject;
import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.account.AccountReqDto;
import com.bank.dto.account.AccountRespDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class) // 1-1. 전체를 메모리에 띄울 필요는 없기 떄문에 @ExtendWith(MockitoExtension.class)로 Service만 메모리에 띄운다
public class AccountServiceTest extends DummyObject {

	// 1-2. UserRepository를 가짜환경에 띄운다. 
	@Mock
	private UserRepository userRepository;
	
	// 1-3. AccountRepository를 가짜환경에 띄운다.
	@Mock
	private AccountRepository accountRepository;
	
	@Spy
	private BCryptPasswordEncoder passwordEncoder;
	
	// 1-4.  1-2, 1-3에서 띄운 가짜환경에 있는 UserRepository와 AccountRepository를 주입해준다.(@InjectMocks로 등록되어 있으면 @Mock 띄워 있는 모든것들을 주입한다)
	@InjectMocks
	private AccountService accountService;
	
	
	@Spy  // 1-5. @Spy는 진짜 객체를 가짜환경에 주입
	private ObjectMapper om;
	
	@Test  // 2023-06-22
	public void 계좌등록_test() throws Exception {
		// given
		Long id = 1L;
		
		AccountReqDto accountReqDto = new AccountReqDto();
		accountReqDto.setNumber("1111");
		accountReqDto.setPassword("1234");
		
		// stub(가짜환경) 1
		User ssar = newMockUser(id, "ssar", "쌀");
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(ssar));
		
		// stub(가짜환경) 2
		when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());
		
		// stub(가짜환경) 3
		Account ssarAccount = newMockAccount(id, "1111", 1000L, ssar);
		when(accountRepository.save(any())).thenReturn(ssarAccount);
		
		// when
		AccountRespDto accountRespDto = accountService.createAccount(accountReqDto, ssar.getUsername());
		String responseBody = om.writeValueAsString(accountRespDto);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		assertThat(accountRespDto.getNumber()).isEqualTo(accountReqDto.getNumber());
	}
	
}
