package com.apipaymybuddy.app.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apipaymybuddy.app.exceptions.UserCreationException;
import com.apipaymybuddy.app.exceptions.UserEmailException;
import com.apipaymybuddy.app.exceptions.UserIdException;
import com.apipaymybuddy.app.models.User;
import com.apipaymybuddy.app.models.UserInfos;
import com.apipaymybuddy.app.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

	@Autowired
	private UserService userService;


	/**
	 * getUser - Get the user entity referred by this email
	 * 
	 * @param email the email of the user to search
	 * @param request   http servlet request
	 * @param response  http servlet response
	 *                  page
	 * @return the user's entity object
	 * @throws IOException
	 */
	@GetMapping("/userid/{userId}")
	public ResponseEntity<User> getUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("userId") final int  userId)
			throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());


		Optional<User> optUser = userService.findById(userId);
		if (optUser.isEmpty()) {

			log.error("User with Email ({}) does not exist.", userId);
			throw new UserIdException(userId);
		}


		log.debug("User with Email ({}) exist.", userId);


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", response.getStatus(), request.getMethod(),
				request.getRequestURI());

		return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
	}
	
	
	/**
	 * getUser - Get the user entity referred by this email
	 * 
	 * @param email the email of the user to search
	 * @param request   http servlet request
	 * @param response  http servlet response
	 *                  page
	 * @return the user's entity object
	 * @throws IOException
	 */
	@GetMapping("/user/{email}")
	public ResponseEntity<User> getUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("email") final String email)
			throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());


		Optional<User> optUser = userService.findByMail(email);
		if (optUser.isEmpty()) {

			log.error("User with Email ({}) does not exist.", email);
			throw new UserEmailException(email);
		}


		log.debug("User with Email ({}) exist.", email);


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", response.getStatus(), request.getMethod(),
				request.getRequestURI());

		return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
	}

	
	/**
	 * createUser - Manage user account creation
	 * 
	 * @param user, the user to be created
	 * @param request, http servlet request                   
	 * @param user, object to operate creation 
	 * @return a empty HTTP response
	 * 
	 */
	@PostMapping("/user")
	public ResponseEntity<Void> createUser(HttpServletRequest request, @RequestBody UserInfos userInfos) throws IOException {

		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());

		log.debug("RequestBody:({})", userInfos.toString());

		ResponseEntity<Void> responseEntity = null;

		log.debug("Create a user: ({}) ", userInfos);
		log.info("user creation service call");

		boolean isOk = userService.createUser(userInfos);
		
		// creation Ko!
		if (!isOk) {

			log.error("The creation of user ({}) could not be realized", userInfos);
			throw new UserCreationException();

		}

		// creation Ok!
		responseEntity = new ResponseEntity<Void>(HttpStatus.CREATED);
		log.info("the user account ({}) creation have been realized", userInfos);


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", responseEntity.getStatusCode(), request.getMethod(),
				request.getRequestURI());

		return responseEntity;
	}

	
	/**
	 * updateUser - Manage update request of informations user
	 * 
	 * @param               idAccount, the account id to be debited
	 * @param request       http servlet request // * @param response http servlet
	 *                      response
	 * @param transferInfos all transfer informations to operate transfer //
	 *                      * @return redirect ModelAndView to /account/{idAccount}
	 * @return a empty HTTP response
	 * 
	 */
	@PostMapping("/user/{userId}")
	public ResponseEntity<Void> updateUser(HttpServletRequest request, @PathVariable int userId,
			@RequestBody UserInfos userInfos) throws IOException {


		log.info("Requete HTTP ({}), Uri: ({})", request.getMethod(), request.getRequestURI());

		log.debug("RequestBody:({})", userInfos.toString());

		ResponseEntity<Void> responseEntity = null;

		// check user exist
		Optional<User> userOptional = userService.findById(userId);
		if (userOptional.isEmpty()) {

			log.error("User with ID ({}) does not exist.", userId);
			throw new UserIdException(userId);

		}

		// check that all the required conditions are ok

		User user = userOptional.get();

		log.debug("Update informations user: ({}) ", user);
		log.debug("with userInfos: ({}) ", userInfos);


		log.info("updateUser service call");

		boolean isOk =  userService.updateUser(userInfos);
		
		// update Ko!
		if (!isOk) {

			log.error("The update of user ({}) could not be realized", userInfos);
			throw new UserCreationException();

		}

		// creation Ok!
		responseEntity = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		log.info("the user account ({}) update have been realized", userInfos);


		log.info("Reponse ({}) requete HTTP ({}), Uri: ({})", responseEntity.getStatusCode(), request.getMethod(),
				request.getRequestURI());

		return responseEntity;

	}

 
	/**
	 * handleBadRequestException - bad request exception manager.
	 * 
	 * @param ex    exception object throwed.
	 * @param model data transmitted to the error view
	 * @return the template error web page
	 * 
	 */
	@ExceptionHandler({ UserEmailException.class })
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
	@ExceptionHandler({ UserCreationException.class })
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
