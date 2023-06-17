package com.bank.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
public class AccountController {

	@GetMapping("/new")
	public String createForm() {
		return "account/accountForm";
	}
	
	@GetMapping("")
	public String accountList() {
		return "account/accountList";
	}
}
