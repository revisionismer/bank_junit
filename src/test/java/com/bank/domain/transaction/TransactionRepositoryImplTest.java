package com.bank.domain.transaction;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.dummy.DummyObject;
import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;

@Transactional
@ActiveProfiles("dev")  // 1-3. dev
@AutoConfigureMockMvc  // 1-2. Mock(가짜) 환경에 MockMvc를 등록해놓는다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. SpringBootTest를 MOCK환경으로 실행한다는 뜻.
public class TransactionRepositoryImplTest extends DummyObject {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private EntityManager em;

	@BeforeEach
	public void setUp() {
		autoIncrementReset();
		dataSetting();
	}
	
	@Test
	public void dataJpa_test1() {
		
		List<Transaction> transactionList = transactionRepository.findAll();
		
		transactionList.forEach( (transaction) -> {
			System.out.println("===========================================");
			
			System.out.println("테스트 : " + transaction.getId());
			System.out.println("테스트 : " + transaction.getAmount());
			System.out.println("테스트 : " + transaction.getReceiver());
			System.out.println("테스트 : " + transaction.getSender());
			System.out.println("테스트 : " + transaction.getDepositAccountBalance());
			System.out.println("테스트 : " + transaction.getGubun());
			System.out.println("테스트 : " + transaction.getTel());
			
			if(transaction.getWithdrawAccount() != null) {
				System.out.println("테스트 : " + transaction.getWithdrawAccount().getId());
				System.out.println("테스트 : " + transaction.getWithdrawAccount().getNumber());
				System.out.println("테스트 : " + transaction.getWithdrawAccount().getUser().getUsername());
			} else {
				System.out.println("테스트 : null");
			}
			
		});
	}
	
	@Test
	public void dataJpa_test2() {
		
		List<Transaction> transactionList = transactionRepository.findAll();
		
		transactionList.forEach( (transaction) -> {
			System.out.println("===========================================");
			
			System.out.println("테스트 : " + transaction.getId());
			System.out.println("테스트 : " + transaction.getAmount());
			System.out.println("테스트 : " + transaction.getReceiver());
			System.out.println("테스트 : " + transaction.getSender());
			System.out.println("테스트 : " + transaction.getDepositAccountBalance());
			System.out.println("테스트 : " + transaction.getGubun());
			System.out.println("테스트 : " + transaction.getTel());
			
			if(transaction.getWithdrawAccount() != null) {
				System.out.println("테스트 : " + transaction.getWithdrawAccount().getId());
				System.out.println("테스트 : " + transaction.getWithdrawAccount().getNumber());
				System.out.println("테스트 : " + transaction.getWithdrawAccount().getUser().getUsername());
			} else {
				System.out.println("테스트 : null");
			}
			
		});
	}
	
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
		// 60강부터 다시
	}
	
	private void autoIncrementReset() {
		em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
}
