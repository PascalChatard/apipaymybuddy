package com.paymybuddy.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.app.exceptions.AccountIdException;
import com.paymybuddy.app.exceptions.InvalidTransferAmountException;
import com.paymybuddy.app.exceptions.TransferDescriptionNotFoundException;
import com.paymybuddy.app.exceptions.UnrealizedAddNewConnectionException;
import com.paymybuddy.app.exceptions.UnrealizedTransferException;
import com.paymybuddy.app.exceptions.UserIdException;
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
	 * getAccount - Display account informations referred by this id
	 * 
	 * @param idAccount the id of the account to display
	 * @param request   http servlet request
	 * @param response  http servlet response
	 * @return the template account web page
	 * @throws IOException
	 */
	@GetMapping("/account/{id}")
	public String getAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") final int accountId, Model model)
			throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());


		Optional<Account> optAccount = accountService.findById(accountId);
		if (optAccount.isEmpty()) {

			log.error("Account with ID ({}) does not exist.", accountId);
			throw new AccountIdException(accountId);
		}

		model.addAttribute("account", optAccount.get());

		log.debug("Account with ID ({}) exist.", accountId);


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", response.getStatus(), request.getMethod(),
				request.getRequestURI());

		return "accountPage";
	}


	/**
	 * makeTransfer - Manage transfer request of money
	 * 
	 * @param               idAccount, the account id to be debited
	 * @param request       http servlet request
	 * @param response      http servlet response
	 * @param transferInfos all transfer informations to operate transfer
	 * @return redirect ModelAndView to /account/{idAccount}
	 * 
	 */
	@PostMapping("/account/{accountId}")
	public ModelAndView makeTransfer(HttpServletRequest request, @PathVariable int accountId,
			@ModelAttribute("transferInfos") TransferInfos transferInfos) throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());

		log.debug("RequestBody:({})", transferInfos.toString());

		ResponseEntity<Void> responseEntity = null;

		// check account exist
		Optional<Account> accountOptional = accountService.findById(accountId);
		if (accountOptional.isEmpty()) {

			log.error("Account with ID ({}) does not exist.", accountId);
			throw new AccountIdException(accountId);

		}

		// check user exist
		Optional<User> userOptional = userService.findById(transferInfos.getBeneficiaryUserId());
		if (userOptional.isEmpty()) {

			log.error("User with ID ({}) does not exist.", transferInfos.getBeneficiaryUserId());
			throw new UserIdException(transferInfos.getBeneficiaryUserId());

		}

		// check there is transfer description in request
		if ((transferInfos.getTransferDescription() == null) || transferInfos.getTransferDescription().isEmpty()) {

			log.error("Transfer description is marked non-null but is null.");
			throw new TransferDescriptionNotFoundException();

		}

		// check there is transfer amount in request
		if (transferInfos.getTransferAmout() <= 0) {

			log.error("Invaid value, the amount cannot be negative or equal to zero ({}).",
					transferInfos.getTransferAmout());
			throw new InvalidTransferAmountException();
		}

		// check that all the required conditions are ok

		User user = userOptional.get();
		Account account = accountOptional.get();

		log.debug("Make a transfer from account: ({}) ", account);
		log.debug("to user: ({}) ", user);
		log.debug("with description ({}) and amount ({})", transferInfos.getTransferDescription(),
					transferInfos.getTransferAmout());

		log.info("makeTransfer service call");

		Account accountUpdated = accountService.makeTransfer(account, user, transferInfos.getTransferDescription(),
					transferInfos.getTransferAmout(), rateService.findById(1).get());
		// Transfer Ko!
		if (accountUpdated == null) {

			log.error("The transfer ({}) of amount ({}) could not be realized",
						transferInfos.getTransferDescription(), transferInfos.getTransferAmout());
			throw new UnrealizedTransferException(transferInfos.getTransferDescription(),
						transferInfos.getTransferAmout());

		}

		// Transfer Ok!
		responseEntity = new ResponseEntity<Void>(HttpStatus.OK);
		log.info("The transfer ({}) of amount ({}) be realized", transferInfos.getTransferDescription(),
						transferInfos.getTransferAmout());


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", responseEntity.getStatusCode(), request.getMethod(),
				request.getRequestURI());

		return new ModelAndView("redirect:/account/" + accountId);
	}


	/**
	 * addConnection - Display view for select and add new connection to account
	 * referred by this id
	 * 
	 * @param id       The id of the account
	 * @param request  http servlet request
	 * @param response http servlet response
	 * @param model    data transmitted to the view (account id, user list)
	 * @return the add connection web page template
	 * @throws IOException
	 */
	@GetMapping("/account/connection/{id}")
	public String addConnection(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") final int accountId, Model model) throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());

		Optional<Account> optAccount = accountService.findById(accountId);
		if (optAccount.isEmpty()) {

			log.error("Account with ID ({}) does not exist.", accountId);
			throw new AccountIdException(accountId);
		}

		model.addAttribute("account", optAccount.get());

		log.debug("Account with ID ({}) exist.", accountId);

		// searches for Users who are not account owner and who are not in connections
		List<User> availableUsers = accountService.getAvailableUserForConnection(userService.findAll(),
				optAccount.get());
		log.debug("Are there available users for the connection? ({})", availableUsers.isEmpty() ? "no" : "yes");

		model.addAttribute("users", availableUsers);

		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", response.getStatus(), request.getMethod(),
				request.getRequestURI());

		return "addConnectionPage";
	}


	/**
	 * updateConnection - Add new connection to account
	 * 
	 * @param accountId the id of the account
	 * @param userId    the id of the user who becoming a new connection
	 * @param request   http servlet request
	 * @param response  http servlet response
	 * @return redirect ModelAndView to /account/{idAccount}
	 * @throws IOException
	 */
	@GetMapping("/account/{id1}/connection/{id2}")
	public ModelAndView updateConnection(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id1") final int accountId, @PathVariable("id2") final int userId)
			throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());


		Optional<Account> optAccount = accountService.findById(accountId);
		if (optAccount.isEmpty()) {

			log.error("Account with ID ({}) does not exist.", accountId);
			throw new AccountIdException(accountId);

		}

		Optional<User> optUser = userService.findById(userId);
		if (optUser.isEmpty()) {

			log.error("User with ID ({}) does not exist.", accountId);
			throw new UserIdException(userId);

		}

		Account account = optAccount.get();
		User user = optUser.get();

		log.debug("Account with ID ({}) and User with ID ({}) exist.", accountId, userId);

		account.getConnections().add(user);
		Account accountUpdated = accountService.save(account);

		if (accountUpdated == null) {

			log.error("The add new connection with userId ({}) to account ({}) could not be realized", userId,
						accountId);
			throw new UnrealizedAddNewConnectionException(userId, accountId);

		}

		log.debug("The add new connection with userId ({}) to account ({})  be realized", userId, accountId);


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", response.getStatus(), request.getMethod(),
				request.getRequestURI());

		return new ModelAndView("redirect:/account/" + accountId);
	}


	/**
	 * handleBadRequestException - bad request exception manager.
	 * 
	 * @param ex    exception object throwed.
	 * @param model data transmitted to the error view
	 * @return the template error web page
	 * 
	 */
	@ExceptionHandler({ AccountIdException.class, UserIdException.class, TransferDescriptionNotFoundException.class,
			InvalidTransferAmountException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleBadRequestException(Exception ex, Model model) {

		log.error("Exception: {} -> {}", ex.getClass(), ex.getMessage());

		model.addAttribute("titrePage", "***! Error page !***");
		model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
		model.addAttribute("errorDescription", ex.getMessage());
		model.addAttribute("status", HttpStatus.BAD_REQUEST.value());

		return "error";
	}


	/**
	 * handleInternalServerErrorException - unrealized transfer exception manager.
	 * 
	 * @param ex    unrealized transfer throwabled object exception.
	 * @param model data transmitted to the error view
	 * @return the template error web page
	 * 
	 */
	@ExceptionHandler({ UnrealizedTransferException.class, UnrealizedAddNewConnectionException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleInternalServerErrorException(Exception ex, Model model) {

		log.error("Exception: {} -> {}", ex.getClass(), ex.getMessage());

		model.addAttribute("titrePage", "***! Error page !***");
		model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		model.addAttribute("errorDescription", ex.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

		return "error";
	}

}
