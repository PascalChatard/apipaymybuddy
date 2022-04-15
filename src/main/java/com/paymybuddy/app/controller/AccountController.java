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
import com.paymybuddy.app.models.User;
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
	public ResponseEntity<Account> getAccount(HttpServletRequest request, @PathVariable("id") final Integer idAccount) {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());

		ResponseEntity<Account> responseEntity = null;

		Optional<Account> account = accountService.findById(idAccount);
		if (account.isPresent()) {

			log.info("Account with ID ({}) exist.", idAccount);
			responseEntity = ResponseEntity.ok(account.get());

		} else {

			log.error("Account with ID ({}) does not exist.", idAccount);
			responseEntity = new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
		}

		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", responseEntity.getStatusCode(), request.getMethod(),
				request.getRequestURI());

		return responseEntity;
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

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());

		log.debug("RequestBody:({})", transferInfos.toString());

		ResponseEntity<Void> responseEntity = null;
		boolean statusRequest = true;

		// check account exist
		Optional<Account> accountOptional = accountService.findById(idAccount);
		if (accountOptional.isEmpty()) {

			statusRequest = false;
			log.error("Account with ID ({}) does not exist.", idAccount);
			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

		}

		// check user exist
		Optional<User> userOptional = userService.findById(transferInfos.getBeneficiaryUserId());
		if (statusRequest && userOptional.isEmpty()) {

			statusRequest = false;
			log.error("User with ID ({}) does not exist.", transferInfos.getBeneficiaryUserId());
			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		// check there is transfer description in request
		if (statusRequest && (transferInfos.getTransferDescription() == null)) {

			statusRequest = false;
			log.error("Transfer description is marked non-null but is null.");
			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		// check there is transfer amount in request
		if (statusRequest && (transferInfos.getTransferAmout() == 0)) {

			statusRequest = false;
			log.error("No transfer amount value.");
			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		// check that all the required conditions are ok
		if (statusRequest) {

			User user = userOptional.get();
			Account account = accountOptional.get();

			log.debug("Make a transfer from account: ({}) ", account);
			log.debug("to user: ({}) ", user);
			log.debug("with description ({}) and amount ({})", transferInfos.getTransferDescription(),
					transferInfos.getTransferAmout());
			log.info("makeTransfer service call");

			Account accountUpdated = accountService.makeTransfer(account, user,
					transferInfos.getTransferDescription(), transferInfos.getTransferAmout(),
					rateService.findById(1).get());
			if (accountUpdated == null) {

				responseEntity = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
				log.error("The transfer ({}) of amount ({}) could not be realized",
						transferInfos.getTransferDescription(), transferInfos.getTransferAmout());
			} else {
				responseEntity = new ResponseEntity<Void>(HttpStatus.OK);
				log.info("The transfer ({}) of amount ({}) be realized", transferInfos.getTransferDescription(),
						transferInfos.getTransferAmout());
			}

		}

		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", responseEntity.getStatusCode(), request.getMethod(),
				request.getRequestURI());

		return responseEntity;
	}

}
