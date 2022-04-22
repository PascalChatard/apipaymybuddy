package com.paymybuddy.app.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.TransferInfos;
import com.paymybuddy.app.models.User;
import com.paymybuddy.app.services.AccountService;
import com.paymybuddy.app.services.RateService;
import com.paymybuddy.app.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
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
	 * @throws IOException
	 */
	@GetMapping("/account/{id}")
	public String getAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") final Integer idAccount, Model model)
			throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());


		Optional<Account> optAccount = accountService.findById(idAccount);
		if (optAccount.isPresent()) {

			model.addAttribute("account", optAccount.get());

			log.info("Account with ID ({}) exist.", idAccount);

		} else {

			log.error("Account with ID ({}) does not exist.", idAccount);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "**** Toto ******");
		}

		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", response.getStatus(), request.getMethod(),
				request.getRequestURI());

		return "makeTransferPage";
	}


	/**
	 * makeTransfer - Manage transfer request of money
	 * 
	 * @param idAccount, the account id to be debited
	 * @param transferInfos, all the transfer informations to operate transfer
	 * @return Void ResponseEntity
	 * 
	 */
//	@PutMapping("/account/{idAccount}/transfer")
//	public ResponseEntity<Void> makeTransfer(HttpServletRequest request, @PathVariable int idAccount,
//			@RequestBody TransferInfos transferInfos) {
//
//		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());
//
//		log.debug("RequestBody:({})", transferInfos.toString());
//
//		ResponseEntity<Void> responseEntity = null;
//		boolean statusRequest = true;
//
//		// check account exist
//		Optional<Account> accountOptional = accountService.findById(idAccount);
//		if (accountOptional.isEmpty()) {
//
//			statusRequest = false;
//			log.error("Account with ID ({}) does not exist.", idAccount);
//			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
//
//		}
//
//		// check user exist
//		Optional<User> userOptional = userService.findById(transferInfos.getBeneficiaryUserId());
//		if (statusRequest && userOptional.isEmpty()) {
//
//			statusRequest = false;
//			log.error("User with ID ({}) does not exist.", transferInfos.getBeneficiaryUserId());
//			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
//		}
//
//		// check there is transfer description in request
//		if (statusRequest && (transferInfos.getTransferDescription() == null)) {
//
//			statusRequest = false;
//			log.error("Transfer description is marked non-null but is null.");
//			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
//		}
//
//		// check there is transfer amount in request
//		if (statusRequest && (transferInfos.getTransferAmout() == 0)) {
//
//			statusRequest = false;
//			log.error("No transfer amount value.");
//			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
//		}
//
//		// check that all the required conditions are ok
//		if (statusRequest) {
//
//			User user = userOptional.get();
//			Account account = accountOptional.get();
//
//			log.debug("Make a transfer from account: ({}) ", account);
//			log.debug("to user: ({}) ", user);
//			log.debug("with description ({}) and amount ({})", transferInfos.getTransferDescription(),
//					transferInfos.getTransferAmout());
//			log.info("makeTransfer service call");
//
//			Account accountUpdated = accountService.makeTransfer(account, user,
//					transferInfos.getTransferDescription(), transferInfos.getTransferAmout(),
//					rateService.findById(1).get());
//			if (accountUpdated == null) {
//
//				responseEntity = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
//				log.error("The transfer ({}) of amount ({}) could not be realized",
//						transferInfos.getTransferDescription(), transferInfos.getTransferAmout());
//			} else {
//				responseEntity = new ResponseEntity<Void>(HttpStatus.OK);
//				log.info("The transfer ({}) of amount ({}) be realized", transferInfos.getTransferDescription(),
//						transferInfos.getTransferAmout());
//			}
//
//		}
//
//		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", responseEntity.getStatusCode(), request.getMethod(),
//				request.getRequestURI());
//
//		return responseEntity;
//	}
	@PutMapping("/account/{idAccount}/transfer")
	public ModelAndView makeTransfer(HttpServletRequest request, @PathVariable int idAccount,
			@ModelAttribute TransferInfos transferInfos) {

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

		return new ModelAndView("redirect:/account/" + idAccount);
	}

	@PostMapping("/account/transfer")
	public ModelAndView makeTransfer(@ModelAttribute("transferInfos") TransferInfos transferInfos) {

		System.out.print(transferInfos.getTransferDescription());

		return new ModelAndView("redirect:/account/");
	}

	@InitBinder("transferInfos")
	public void initBinder(WebDataBinder binder) {
//		TransferInfosEditor stringTrimmerEditor = new TransferInfosEditor(true);
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, false));

		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, false));
	}

}
