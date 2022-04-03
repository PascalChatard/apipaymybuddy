package com.paymybuddy.app.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.User;
import com.paymybuddy.app.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService extends GenericService<User> {

	/**
	 * Injects user repository bean
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * findByLastname - Get users with last name matching given lastname
	 * 
	 * @param lastname The lastname of the user to find
	 * @return A Iterable list of users found
	 */
	public Iterable<User> findByLastname(String lastname) {
		log.debug("Debut methode findByLastname, lastname {}", lastname);
		return userRepository.findByLastName(lastname);
	}

	/**
	 * existsByEmail - Checks that a user with this email exists
	 * 
	 * @param mail The e-mail of the user to find
	 * @return true if user with e-mail found, false in other case
	 */
	public boolean existsByEmail(String mail) {
		log.debug("Debut methode existsByEmail, mail {}", mail);
		return (userRepository.getNumberUserByEmail(mail) > 0) ? true : false;
	}

	/**
	 * createUser - Add new user and associate account in data base
	 * 
	 * @param user The new user to add
	 * @return operation status, true if success, false in other case
	 */
	public boolean createUser(User user) {

		log.debug("Debut methode createUser, User {}", user);
		log.info("Création d'un nouveau User et du Acount associé ({})", user);

		// all attributes must be filled in
		if (atLeastOneAttributeIsEmpty(user)) {

			log.error("Echec opération création User, au moins un des attibuts est vide");
			return false;
		}

		// email attribute must not already exist
		if (existsByEmail(user.getMail())) {
			log.error("Echec opération création User, l'adresse mail existe déjà dans la BDD");
			return false;
		}

		// management rule, these attributes must be capitalized
		user.setFirstName(user.getFirstName().toUpperCase());
		user.setLastName(user.getLastName().toUpperCase());
		user.setAddress(user.getAddress().toUpperCase());
		user.setCity(user.getCity().toUpperCase());

		// new account to associate with the new user
		Account account = new Account();
		Date date = new Date(System.currentTimeMillis());
		account.setOpenDate(Date.valueOf(date.toString()));
		account.setSolde(0);
		log.info("Opération création Account associé");

		user.setAccountUser(account);

		boolean statusOperation = save(user).equals(user);
		log.info("Opération création User: {}", statusOperation ? "Succes" : "Echec");
		log.debug("Fin methode createUser");
		return statusOperation;

	}

	/**
	 * atLeastOneAttributeIsEmpty - Test if at least one attribute is empty
	 * 
	 * @param user The object user to test
	 * @return operation status, true if one or more attribute is empty, false if
	 *         all attribute are filled
	 */
	private boolean atLeastOneAttributeIsEmpty(User user) {

		log.trace("Exécute methode atLeastOneAttributeIsEmpty");

		return (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getAddress().isEmpty()
				|| user.getCity().isEmpty() || user.getPhone().isEmpty() || user.getMail().isEmpty()
				|| user.getPassword().isEmpty());
	}

}
