package com.bank.web.api.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.config.auth.PrincipalDetails;
import com.bank.dto.ResponseDto;
import com.bank.dto.transaction.TransactionListRespDto;
import com.bank.service.transaction.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionApiController {

	private final TransactionService transactionService;
	
	@GetMapping("/s/account/{number}/transactionInfo")
	public ResponseEntity<?> readTransactionListByAccountNumber(@PathVariable String number, 
																@RequestParam(value = "gubun", defaultValue = "ALL") String gubun,  
																@RequestParam(value = "page", defaultValue = "0") Integer page,
																@AuthenticationPrincipal PrincipalDetails principalDetails) {
	
		TransactionListRespDto transactionListRespDto = transactionService.readDepositAndWithdrawHistory(principalDetails.getUser().getId(), number, gubun, page);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "입출금 목록보기 성공", transactionListRespDto), HttpStatus.OK);
		
	}
}
