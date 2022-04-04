package com.paymybuddy.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.services.AccountService;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	/**
	 * getAccount - Get one account referred by his id
	 * 
	 * @param id The id of the account
	 * @return An Account object full filled
	 */
	@GetMapping("/account/{id}")
//	@GetMapping(path = "/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Account getAccount(@PathVariable("id") final Integer id) {
		Optional<Account> account = accountService.findById(id);
		if (account.isPresent()) {
			return account.get();
		} else {
			return null;
		}
	}

}
