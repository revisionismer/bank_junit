package com.bank.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.config.auth.PrincipalDetails;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@GetMapping("")
	public String userList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		return "user/userList";
	}
	
}
