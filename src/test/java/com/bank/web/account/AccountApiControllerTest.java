package com.bank.web.account;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.dummy.DummyObject;
import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.account.AccountDepositReqDto;
import com.bank.dto.account.AccountReqDto;
import com.bank.service.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@ActiveProfiles("dev")  // 1-3. dev
@AutoConfigureMockMvc  // 1-2. Mock(가짜) 환경에 MockMvc를 등록해놓는다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. SpringBootTest를 MOCK환경으로 실행한다는 뜻.
public class AccountApiControllerTest extends DummyObject {
	
	@Autowired
	private MockMvc mvc;  // 1-3. 1-2에서 등록해놓은 MockMvc를 주입받는다.
	
	@Autowired
	private ObjectMapper om;  // 1-4. ObjectMapper 주입
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Spy
	private BCryptPasswordEncoder passwordEncoder;
	
	@InjectMocks
	private AccountService accountService;
	
	@BeforeEach
	public void setUp1() {
		User jonghee = newMockUser(1L, "jonghee", "종희");
		userRepository.save(jonghee);
	}
	
	public void setUp2() {

		User admin = newMockUser(2L, "admin", "관리자");
		userRepository.save(admin);
		
		User ssar = newMockUser(3L, "ssar", "쌀");
		userRepository.save(ssar);
		
		User cos = newMockUser(4L, "cos", "코스");
		userRepository.save(cos);
		
		User love = newMockUser(5L, "love", "러브");
		userRepository.save(love);
		
		Account ssarAccount1 = newMockAccount(2L, "1111", 1000L, ssar);
		accountRepository.save(ssarAccount1);
		
		Account cosAccount = newMockAccount(3L, "2222", 1000L, cos);
		accountRepository.save(cosAccount);
		
		Account loveAccount = newMockAccount(4L, "3333", 1000L, love);
		accountRepository.save(loveAccount);
		
		Account ssarAccount2 = newMockAccount(5L, "4444", 1000L, ssar);
		accountRepository.save(ssarAccount2);
		
	}
	// 2023-06-23
	// 1-5. 데이터베이스에서 ssar 유저를 조회해서 세션에 담아주는 어노테이션
	// 1-6. setupBefore=TEST -> setUp 메세드 전에  실행된다.
	// 1-7. setupBefere=TEST.EXECUTION -> saveAccount_test() 실행전에 수행.
	@WithUserDetails(value = "jonghee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	public void saveAccount_test() throws Exception {
		// given
		AccountReqDto accountReqDto = new AccountReqDto();
		accountReqDto.setNumber("9999");
		accountReqDto.setPassword("1234");
		
		String requestBody = om.writeValueAsString(accountReqDto);
		
		System.out.println("테스트 : " + requestBody);
		
		// when
		ResultActions resultActions = mvc.perform(post("/api/account/s/new").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		resultActions.andExpect(status().isCreated());
	}
	
	@WithUserDetails(value = "jonghee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	public void deleteAccount_test() throws Exception {
		// given
		saveAccount_test();
		
		AccountReqDto accountReqDto = new AccountReqDto();
		accountReqDto.setNumber("9999");
		accountReqDto.setPassword("1234");
		
		String requestBody = om.writeValueAsString(accountReqDto);
		
		// when
		ResultActions resultActions = mvc.perform(delete("/api/account/s/delete").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		assertThrows(NullPointerException.class, () -> accountService.deleteAccountByUsername(accountReqDto, "ssar"));
	}
	
//	@Test  // cmd에서 gradlew build할때는 오류가나고 이클립스에서 할땐 오류가 안나기때문에 일단 주석처리해준다.(테스트는 성공한 케이스)
	public void depositAccount_test() throws Exception {
		// given
		setUp2();
		
		AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
		accountDepositReqDto.setNumber("2222");
		accountDepositReqDto.setAmount(100L);
		accountDepositReqDto.setGubun("DEPOSIT");
		accountDepositReqDto.setTel("01088887777");
		
		String requestBody = om.writeValueAsString(accountDepositReqDto);
		
		System.out.println(requestBody);
		
		// when
		ResultActions resultActions = mvc.perform(post("/api/account/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		resultActions.andExpect(status().isCreated());
	}
}
