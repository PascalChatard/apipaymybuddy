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
	 * Gere la requete de mise a jour de l'objet Firestation positionne a l'index id
	 * dans le DataSrc dataSrc, appelle la methode de la couche service.
	 * 
	 * @param id, index de l'objet Firestation a modifier
	 * @param request, la requete a traiter
	 * @param firestation, l'objet Firestation a modifier dans la base de donnees
	 * @return une reference a l'objet modifier dans dataSrc
	 * 
	 */
	@PutMapping("/account/{idAccount}/transfer")
	public ResponseEntity<Void> makeTransfer(HttpServletRequest request, @PathVariable int idAccount,
			@RequestBody int userId, double amount) {

		log.info("Requete HTTP {}, Uri: {}", request.getMethod(), request.getRequestURI());

		Account accountUpdated = accountService.makeTransfer(accountService.findById(idAccount).get(),
				userService.findById(userId).get(), "Libellé du transfert", amount, rateService.findById(1).get());


		ResponseEntity<Void> responseEntity = new ResponseEntity<Void>(HttpStatus.OK);

		return responseEntity;
	}

}
