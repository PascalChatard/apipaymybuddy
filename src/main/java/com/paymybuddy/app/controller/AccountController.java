package com.paymybuddy.app.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.TransferInfos;
import com.paymybuddy.app.services.AccountService;
import com.paymybuddy.app.services.RateService;
import com.paymybuddy.app.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	@Autowired
	private RateService rateService;


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

	/**
	 * makeTransfer - Manage transfer request of money
	 * 
	 * @param idAccount, the account id to be debited
	 * @param transferInfos, all the transfer informations to operate transfer
	 * @return Void ResponseEntity
	 * 
	 */
	@PutMapping("/account/{idAccount}/transfer")
	public ResponseEntity<Void> makeTransfer(HttpServletRequest request, @PathVariable int idAccount,
			@RequestBody TransferInfos transferInfos) {

		log.info("Requete HTTP {}, Uri: {}", request.getMethod(), request.getRequestURI());


		Account accountUpdated = accountService.makeTransfer(accountService.findById(idAccount).get(),
				userService.findById(transferInfos.getBeneficiaryUserId()).get(),
				transferInfos.getTransferDescription(),
				transferInfos.getTransferAmout(), rateService.findById(1).get());


		ResponseEntity<Void> responseEntity = new ResponseEntity<Void>(HttpStatus.OK);

		log.info("Reponse ({}) requete HTTP {}, Uri: {}", responseEntity.getStatusCode(), request.getMethod(),
				request.getRequestURI());

		return responseEntity;
	}

}
