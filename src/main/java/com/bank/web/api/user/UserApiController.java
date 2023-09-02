package com.bank.web.api.user;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.config.auth.PrincipalDetails;
import com.bank.domain.user.User;
import com.bank.dto.ResponseDto;
import com.bank.dto.user.UserReqDto.JoinReqDto;
import com.bank.dto.user.UserReqDto.UserUpdateReqDto;
import com.bank.dto.user.UserRespDto.JoinRespDto;
import com.bank.dto.user.UserRespDto.UserInfoRespDto;
import com.bank.dto.user.UserRespDto.UserUpdateRespDto;
import com.bank.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {
	
	private final  UserService userService;
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {
		
		JoinRespDto joinRespDto = userService.join(joinReqDto);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinRespDto), HttpStatus.CREATED);
	}
	
	@GetMapping("/s/info")
	public ResponseEntity<?> userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserInfoRespDto userInfoRespDto = userService.userInfo(loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, userInfoRespDto.getId() + "번 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
	@PutMapping("/s/update")
	public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid UserUpdateReqDto userUpdateReqDto, BindingResult bindingResult) {
		
		User loginUser = principalDetails.getUser();
		
		System.out.println(userUpdateReqDto);
		
		UserUpdateRespDto userUpdateRespDto = userService.userUpdate(userUpdateReqDto, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, userUpdateRespDto.getId() + "번 유저 정보 수정 성공", userUpdateRespDto), HttpStatus.OK);
	}
}
