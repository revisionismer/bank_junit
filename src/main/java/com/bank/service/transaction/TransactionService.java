package com.bank.service.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.domain.account.Account;
import com.bank.domain.account.AccountRepository;
import com.bank.domain.transaction.Transaction;
import com.bank.domain.transaction.TransactionRepository;
import com.bank.dto.transaction.TransactionListRespDto;
import com.bank.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional 
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	
	@Transactional(readOnly = true)
	public TransactionListRespDto readDepositAndWithdrawHistory(Long userId, String accountNumber, String gubun, int page) {
		// 1-1. 요청 계좌 정보가 DB에 있는지 검증 하기위해 DB에서 가져온다. 만약 없다면 예외메시지를 return
		Account accountPS = accountRepository.findByNumber(accountNumber)
				.orElseThrow(() -> new CustomApiException("해당 계좌를 찾을 수 없습니다."));
		
		// 1-2. 입출금 내역을 확인하려는 사람(로그인 한 사람)이 계좌 소유주가 맞는지 검증, 아니면 예외가 터진다.
		accountPS.checkOwner(userId);
		
		// 1-3. 1-2에서 소유주 확인을 마쳤으니 해당 계좌의 PK를 가져온다.
		Long accountId = accountPS.getId();
		
		// 1-4. 입출금 내역을 조회
		List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountId, gubun, page);
		
		// 1-5. TransactionListRespDto 형태로 return
		return new TransactionListRespDto(accountPS, transactionListPS);
	}
}
