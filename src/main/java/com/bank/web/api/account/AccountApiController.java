package com.bank.web.api.account;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.config.auth.PrincipalDetails;
import com.bank.domain.user.User;
import com.bank.dto.ResponseDto;
import com.bank.dto.account.AccountListRespDto;
import com.bank.dto.account.AccountReqDto;
import com.bank.dto.account.AccountRespDto;
import com.bank.service.account.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountApiController {

	private final AccountService accountService;
	
	@PostMapping("/s/new")
	public ResponseEntity<?> createAccount(@RequestBody @Valid AccountReqDto accountReqDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {

		User loignUser = principalDetails.getUser();
		
		AccountRespDto accountRespDto = accountService.createAccount(accountReqDto, loignUser.getUsername());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 생성 성공", accountRespDto), HttpStatus.CREATED);
	}
	
	// 2023-06-27
	@GetMapping("/s/all")
	public ResponseEntity<?> readAllAccountByUsername(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		AccountListRespDto accountListRespDto = accountService.readAllAccountByUsername(loginUser.getUsername());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "나의 계좌 목록 조회 성공", accountListRespDto), HttpStatus.OK);
	}
}
