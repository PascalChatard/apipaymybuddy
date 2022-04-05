package com.paymybuddy.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.services.AccountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public Account getAccount(@PathVariable("id") final Integer id) {
		log.debug("Debut methode getAccount, arg: Account Id ({})", id);

		Optional<Account> account = accountService.findById(id);
		if (account.isPresent()) {
			log.debug("Fin methode getAccount");

			return account.get();
		} else {
			log.debug("Fin methode getAccount");

			return null;
		}
	}

}
