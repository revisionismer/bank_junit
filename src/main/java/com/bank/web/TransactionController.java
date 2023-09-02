package com.bank.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
	
	@GetMapping("")
	public String transactionList() {
		return "transaction/transactionList";
	}

	@GetMapping("/deposit")
	public String depositForm() {
		return "transaction/depositForm";
	}
	
	@GetMapping("/withdraw")
	public String witdrawForm() {
		return "transaction/withdrawForm";
	}
}
