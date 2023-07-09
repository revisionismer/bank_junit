package com.bank.service.account;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.constant.transaction.TransactionEnum;
import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.transaction.TransactionRepository;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.account.AccountDepositReqDto;
import com.bank.dto.account.AccountDepositRespDto;
import com.bank.dto.account.AccountListRespDto;
import com.bank.dto.account.AccountReqDto;
import com.bank.dto.account.AccountRespDto;
import com.bank.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional  // 집에가서 org.springframework.transaction.annotation.Transactional으로 안되어 있으면 바꿔줘야한다.
@RequiredArgsConstructor
public class AccountService {

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public AccountRespDto createAccount(AccountReqDto accountReqDto, String username) {
		// 1-1. User가 DB에 있는지 검증
		Optional<User> userOp = userRepository.findByUsername(username);
		
		if(userOp.isPresent()) {   // 1-2. 해당 유저가 DB에 있는 유저면 계좌 생성 진행
			
			User user = userOp.get();  // 1-3. 유저를 get해온다.
			
			// 1-4. 요청 계좌 정보가 DB에 있는지 검증 하기위해 Optional로 가져온다.
			Optional<Account> accountOp = accountRepository.findByNumber(accountReqDto.getNumber());
			
			if(accountOp.isPresent()) {  // 1-5. 만들어 줄 계좌가 이미 존재하면
				// 1-6. 익셉션을 터트려준다.
				throw new CustomApiException("이미 존재하는 계좌입니다.");
			} else {
				// 1-7. 존재하지 않는 계좌면 해당 유저 정보로 신규 계좌 생성
				Account accountPS = accountRepository.save(accountReqDto.toEntity(user, passwordEncoder));
				
				// 1-5. DTO로 응답
				return new AccountRespDto(accountPS);
			}
			
		} else {
			throw new CustomApiException("존재하지 않는 회원입니다.");
		}
	}
	// 2023-06-28 
	@Transactional(readOnly = true)
	public AccountListRespDto readAllAccountByUsername(String username) {
		// 2-1.  User가 DB에 있는지 검증
		Optional<User> userOp = userRepository.findByUsername(username);
		
		if(userOp.isPresent()) {   // 2-2. 해당 유저가 DB에 있는 유저면 계좌 생성 진행
			
			User user = userOp.get();  // 2-3. 유저를 get해온다.
			
			Long userId = user.getId(); // 2-4. 해당 유저의 기본키 id값을 가져온다
			
			// 2-5. 2-4의 userId로 해당 userId의 계좌 정보 리스트를 가져온다.
			List<Account> accountList = accountRepository.findAllByUser_id(userId);
			
			// 2-6. AccountListRespDto 형태로 return
			return new AccountListRespDto(user, accountList);
			
			
		} else {
			throw new CustomApiException("존재하지 않는 회원입니다.");
		}
	}
	
	// 2023-06-29
	// 3-1. 계좌 삭제하기
	public void deleteAccountByUsername(AccountReqDto accountReqDto, String username) {
		// 3-2. 실제로 DB에 계좌가 있는지 확인
		Optional<Account> accountOp = accountRepository.findByNumber(accountReqDto.getNumber());
			
		if(accountOp.isPresent()) {  // 3-3. 계좌가 존재하면
				
			Account account = accountOp.get();  // 3-4. get
				
			// 3-5. 전달 받은 아이디(username)로 해당 유저를 가져온다.
			Optional<User> userOp = userRepository.findByUsername(username);
				
			if(userOp.isPresent()) {  // 3-5. 해당 유저가 존재 하면
				User user = userOp.get();  // 3-6. get
					
				Long userId = user.getId();  // 3-7. user의 기본키 id값을 가져온다.
					
				// 3-8. userId로 계좌 소유주 확인 하기(아니면 여기서 익셉션이 터짐)
				account.checkOwner(userId);
					
				// 3-9. 계좌 비밀번호 체크
				if(!passwordEncoder.matches(accountReqDto.getPassword(), account.getPassword())) {
					throw new CustomApiException("계좌 비밀번호가 다릅니다.");
				}
				
				// 3-10. 소유자가 확인되었으니 계좌 삭제.
				accountRepository.deleteById(account.getId());
			}
				
		} else {
			throw new CustomApiException("없는 계좌입니다.");
		}
			
	}
	
	// 2023-07-04
	// 4-1. 계좌에 입금하기 : 인증이 필요 없다.
	@Transactional
	public AccountDepositRespDto depositIntoAccount(AccountDepositReqDto accountDepositReqDto) {
		// 4-2. 입금 금액이 0원인지 체크
		if(accountDepositReqDto.getAmount() == 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
		}
		
		// 4-3. 입금 계좌 확인
		Optional<Account> depositAccountOp = accountRepository.findByNumber(accountDepositReqDto.getNumber());
		
		if(depositAccountOp.isPresent()) {  // 4-5. 입금할 계좌가 존재하면
			
			Account accountPS = depositAccountOp.get();  // 4-6. get
			
			accountPS.deposit(accountDepositReqDto.getAmount());  // 4-7. 입금(해당 계좌의 balance 조정 - update문 : 더티체킹)
			
			Transaction transaction = Transaction.builder()  // 4-8. 거래 내역을 남겨주기위해 거래내역 생성(마지막에 들어가야 한다.)
					.depositAccount(accountPS)
					.withdrawAccount(null)
					.depositAccountBalance(accountPS.getBalance())
					.withdrawAccountBalance(null)
					.amount(accountDepositReqDto.getAmount())
					.gubun(TransactionEnum.DEPOSIT)
					.sender("ATM")
					.receiver(accountPS.getNumber())
					.tel(accountDepositReqDto.getTel())
					.build();
			
			Transaction transactionPS = transactionRepository.save(transaction);  // 4-9. 거래내역 정보 저장한 후 영속화시킨 엔티티를 반환(더티 체킹)
			
			return new AccountDepositRespDto(accountPS, transactionPS);  // 4-10. 영속화된 계좌 정보와 거래 내역 정보를 가지고 응답 객체를 만들어 반환
			
		} else {
			throw new CustomApiException("없는 계좌입니다.");
		}
		
	}	
	
}
