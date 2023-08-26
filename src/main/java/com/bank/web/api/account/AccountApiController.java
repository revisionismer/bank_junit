package com.bank.web.api.account;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.config.auth.PrincipalDetails;
import com.bank.domain.user.User;
import com.bank.dto.ResponseDto;
import com.bank.dto.account.AccountDepositReqDto;
import com.bank.dto.account.AccountDepositRespDto;
import com.bank.dto.account.AccountDetailRespDto;
import com.bank.dto.account.AccountListRespDto;
import com.bank.dto.account.AccountReqDto;
import com.bank.dto.account.AccountRespDto;
import com.bank.dto.account.AccountTransferReqDto;
import com.bank.dto.account.AccountTransferRespDto;
import com.bank.dto.account.AccountWithdrawReqDto;
import com.bank.dto.account.AccountWithdrawRespDto;
import com.bank.service.account.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountApiController {

	private final AccountService accountService;
	
	@GetMapping("/s/list")
	public ResponseEntity<?> selectAccountNumber(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		
		User loginUser = principalDetails.getUser();
		
		AccountListRespDto accountListRespDto = accountService.readAccountNumberList(loginUser.getUsername());
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getFullname() + "님의 보유 계좌 정보 리스트 불러오기 성공", accountListRespDto), HttpStatus.OK);
	}
	
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
	
	@DeleteMapping("/s/delete")
	public ResponseEntity<?> deleteAccountByUsername(@RequestBody @Valid AccountReqDto accountReqDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		accountService.deleteAccountByUsername(accountReqDto, loginUser.getUsername());
		
		return new ResponseEntity<>(new ResponseDto<>(1, accountReqDto.getNumber() + "번 계좌 삭제 성공", null), HttpStatus.OK);
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto, BindingResult bindingResult) {
		
		AccountDepositRespDto accountDepositRespDto = accountService.depositIntoAccount(accountDepositReqDto);
		
		return new ResponseEntity<>(new ResponseDto<>(1, accountDepositReqDto.getNumber() + "번 계좌에 입금하기 성공", accountDepositRespDto), HttpStatus.CREATED);
	}
	
	@PostMapping("/s/withdraw")
	public ResponseEntity<?> withdrawAccount(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid AccountWithdrawReqDto accountWithdrawReqDto, BindingResult bindingResult) {
		
		User loginUser = principalDetails.getUser();
		
		AccountWithdrawRespDto accountWithdrawRespDto = accountService.withdrawInAccount(accountWithdrawReqDto, loginUser.getUsername());
		
		return new ResponseEntity<>(new ResponseDto<>(1, accountWithdrawReqDto.getNumber() + "번 계좌에서 출금하기 성공", accountWithdrawRespDto), HttpStatus.OK);
	}
	
	@PostMapping("/s/transfer")
	public ResponseEntity<?> transferAccount(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid AccountTransferReqDto accountTransferReqDto, BindingResult bindingResult) {
		
		User loginUser = principalDetails.getUser();
		
		AccountTransferRespDto accountTransferRespDto = accountService.transferToAccount(accountTransferReqDto, loginUser.getUsername());
		
		return new ResponseEntity<>(new ResponseDto<>(1, accountTransferReqDto.getWithdrawNumber() + "번 계좌에서 " + accountTransferReqDto.getDepositNumber() + "번 계좌로 " + accountTransferReqDto.getAmount() + "원 이체하기 성공", accountTransferRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/s/{number}/info")
	public ResponseEntity<?> viewAccountDetail(@PathVariable String number,
											   @RequestParam(value = "page", defaultValue = "0") Integer page,
											   @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		AccountDetailRespDto accountDetailRespDto = accountService.readAccountDetail(number, loginUser.getId(), page);
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getFullname() + "님의 " + number + "번 계좌 상세 내역 보기", accountDetailRespDto), HttpStatus.OK);
	}
}
