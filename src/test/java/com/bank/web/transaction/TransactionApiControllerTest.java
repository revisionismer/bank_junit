package com.bank.web.transaction;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.StandardCharsets;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.dummy.DummyObject;
import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.transaction.TransactionRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@ActiveProfiles("dev")  // 1-3. dev
@AutoConfigureMockMvc  // 1-2. Mock(가짜) 환경에 MockMvc를 등록해놓는다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. SpringBootTest를 MOCK환경으로 실행한다는 뜻.
public class TransactionApiControllerTest extends DummyObject {
	
	@Autowired
	private MockMvc mvc;  
	
	@Autowired
	private ObjectMapper om; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private EntityManager em;
	
	@BeforeEach
	public void setUp() {
		dataSetting();
		autoIncrementReset();
		em.clear();
	}
	
	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	public void findTransactionList_test() throws Exception {
		// given
		String number = "1111";
		String gubun = "ALL";
		String page = "0";
		
		// when
		ResultActions resultActions = mvc.perform(get("/api/transaction/s/account/" + number + "/transactionInfo")
												.param("gubun", gubun)
												.param("page", page));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		om.writeValueAsString(responseBody);
		
		// then
		resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
		resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
		resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
		resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
	}
	
	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	public void viewAccountDetail_test() throws Exception {
		// given
		String number = "1111";
		String page = "0";
		
		// when
		ResultActions resultActions = mvc.perform(get("/api/account/s/" + number + "/info")
												.param("page", page));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		om.writeValueAsString(responseBody);
		
		// then
		resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
		resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
		resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
		resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
	}
	
	// 2023-08-07 : 여기까지
	
	private void dataSetting() {
		User ssar = userRepository.save(newUser("ssar", "1234", "ssar_test@nate.com", "쌀"));
		User cos = userRepository.save(newUser("cos", "1234", "cos_test@nate.com", "코스"));
		User love = userRepository.save(newUser("love", "1234", "love_test@nate.com", "러브"));
		User admin = userRepository.save(newUser("admin", "1234", "admin_test@nate.com", "관리자"));
		
		Account ssar_Account1 = accountRepository.save(newAccount("1111", 1000L, ssar));
		Account cos_Account = accountRepository.save(newAccount("2222", 1000L, cos));
		Account love_Account = accountRepository.save(newAccount("3333", 1000L, love));
		Account ssar_Account2 = accountRepository.save(newAccount("4444", 1000L, admin));
		
		System.out.println(ssar_Account2);
		
		Transaction withdrawTransaction1 = transactionRepository.save(newWithdrawTransaction(ssar_Account1, accountRepository));
		Transaction depositTransaction1 = transactionRepository.save(newDepositTransaction(cos_Account, accountRepository));
		
		System.out.println(withdrawTransaction1);
		System.out.println(depositTransaction1);
		
		Transaction transferTransaction1 = transactionRepository.save(newTransferTransaction(ssar_Account1, cos_Account, accountRepository));
		Transaction transferTransaction2 = transactionRepository.save(newTransferTransaction(ssar_Account1, love_Account, accountRepository));
		Transaction transferTransaction3 = transactionRepository.save(newTransferTransaction(cos_Account, ssar_Account1, accountRepository));
				
		System.out.println(transferTransaction1);
		System.out.println(transferTransaction2);
		System.out.println(transferTransaction3);
	}
	
	private void autoIncrementReset() {
		em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}

}