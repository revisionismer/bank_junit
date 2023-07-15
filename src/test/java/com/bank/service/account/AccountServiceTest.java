package com.bank.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
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
import com.bank.domain.transaction.Transaction;
import com.bank.domain.transaction.TransactionRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.account.AccountDepositReqDto;
import com.bank.dto.account.AccountDepositRespDto;
import com.bank.dto.account.AccountReqDto;
import com.bank.dto.account.AccountRespDto;
import com.bank.handler.exception.CustomApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class) // 1-1. 전체를 메모리에 띄울 필요는 없기 떄문에 @ExtendWith(MockitoExtension.class)로 Service만 메모리에 띄운다
public class AccountServiceTest extends DummyObject {

	// 1-2. UserRepository를 가짜환경에 띄운다. 
	@Mock
	private UserRepository userRepository;
	
	// 1-3. AccountRepository를 가짜환경에 띄운다.
	@Mock
	private AccountRepository accountRepository;
	
	// 1-6. TransactionRepository를 가짜환경에 띄운다
	@Mock
	private TransactionRepository transactionRepository;
	
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
	
	@Test  
	public void 계좌삭제_test() throws Exception {
		// given
		String number = "12345";
	
		AccountReqDto accountReqDto = new AccountReqDto();
		accountReqDto.setNumber(number);
		
		// stub
		User ssar = newMockUser(1L, "ssar", "쌀");
		lenient().when(userRepository.save(any())).thenReturn(ssar);
		lenient().when(userRepository.findByUsername(any())).thenReturn(Optional.of(ssar));
		
		User cos = newMockUser(2L, "cos", "코스");
		lenient().when(userRepository.save(any())).thenReturn(cos);
		lenient().when(userRepository.findByUsername(any())).thenReturn(Optional.of(cos));
		
		Account ssarAccount = newMockAccount(1L, "1111", 1000L, ssar);
		lenient().when(accountRepository.save(any())).thenReturn(ssarAccount);
		lenient().when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));
		
		// 가정 : when 함수를 사용하면 단순히 "어떤 동작을 할 때~"라는 명시만 주어진다.
		// lenient() : junit5에서부터 발생하는 Unnecessary stubbings detected. 예외 해결 방법. -> stubbing이 미사용될 수 있음을 표시하는 메서드
        
		// when
		
		// then
		assertThrows(CustomApiException.class, () -> accountService.deleteAccountByUsername(accountReqDto, cos.getUsername()));
	}
	
	// 2023-07-13 : 여기까지
	@Test
	public void 계좌입금_test() throws Exception {
		// given
		AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
		accountDepositReqDto.setNumber("1111");
		accountDepositReqDto.setAmount(100L);
		accountDepositReqDto.setGubun("DEPOSIT");
		accountDepositReqDto.setTel("01088887777");
		
		// stub 1
		User ssar = newMockUser(1L, "ssar", "쌀");
		Account ssarAccount = newMockAccount(1L, "1111", 1000L, ssar);
		System.out.println("테스트 : 이체 전 계좌 잔액(Account)  : " + ssarAccount.getBalance());
		lenient().when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));
		
		// stub 2 : stub이 진행될 때마다 연관된 객체는 새로 만들어서 주입해야 한다.
		Transaction transaction = newMockDepositTransaction(1L, ssarAccount);
		lenient().when(transactionRepository.save(any())).thenReturn(transaction);
		
		// when
		AccountDepositRespDto accountDepositRespDto = accountService.depositIntoAccount(accountDepositReqDto);
		System.out.println("테스트 : 이체 전 계좌 잔액(Transaction) : " + accountDepositRespDto.getTransaction().getDepositAccountBalance());
		System.out.println("테스트 : 이체 금액 : " + accountDepositRespDto.getTransaction().getAmount());
		System.out.println("테스트 : 최종 계좌쪽 잔액 -> " + ssarAccount.getBalance());
		
		// then
		assertThat(ssarAccount.getBalance()).isEqualTo(1100L);
	}
	
	@Test
	public void 계좌입금_번외test1() throws Exception {
		// given
		AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
		accountDepositReqDto.setNumber("1111");
		accountDepositReqDto.setAmount(100L);
		accountDepositReqDto.setGubun("DEPOSIT");
		accountDepositReqDto.setTel("01088887777");
		
		// stub 1
		User ssar = newMockUser(1L, "ssar", "쌀");
		Account ssarAccount = newMockAccount(1L, "1111", 1000L, ssar);
		System.out.println("테스트 : 이체 전 계좌 잔액(Account)  : " + ssarAccount.getBalance());
		lenient().when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));
		
		// stub 2 : stub이 진행될 때마다 연관된 객체는 새로 만들어서 주입해야 한다.
		Transaction transaction = newMockDepositTransaction(1L, ssarAccount);
		lenient().when(transactionRepository.save(any())).thenReturn(transaction);
		
		// when
		AccountDepositRespDto accountDepositRespDto = accountService.depositIntoAccount(accountDepositReqDto);
		
		String responseBody = om.registerModule(new JavaTimeModule()).writeValueAsString(accountDepositRespDto);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		assertThat(ssarAccount.getBalance()).isEqualTo(1100L);
	}
	
	@Test
	public void 계좌입금_번외test2() throws Exception {
		// given
		Account account = newMockAccount(1L, "1111", 1000L, null);
		
		Long amount = 100L;
		
		if(amount <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
		}
		
		// when
		account.deposit(amount);
		
		// then
		assertThat(account.getBalance()).isEqualTo(1100L);
	}
	
	@Test
	public void 계좌출금_test() throws Exception {
		// given
		Long amount = 100L;
		
		User ssar = newMockUser(1L, "ssar", "쌀");
		Account ssarAccount = newMockAccount(1L, "1111", 1000L, ssar);
		
		// when
		if(amount <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 출금할 수 없습니다.");
		}
		
		ssarAccount.checkOwner(1L);
		ssarAccount.checkSamePassword("1234", passwordEncoder);
		ssarAccount.checkBalance(amount);
		ssarAccount.withdraw(amount);
		
		// then
		assertThat(ssarAccount.getBalance()).isEqualTo(900L);
	}
	// 서비스 테스트를 진행한 것은 기술적인 테크닉을 보여주려고 해본 것.
	// 전체 서비스를 테스트 하고 싶다면, 내가 지금 무엇을 여기서 테스트해야할지부터 명확히 구분(책임 분리)
	// DB관련된 것을 조회했을 때, 그 값을 통해서 어떤 비즈니스 로직이 흘러가는 것이 있을때 -> stub으로 정의해서 테스트 해보면 된다.
}
