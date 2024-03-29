package com.apipaymybuddy.app.services;

import java.sql.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apipaymybuddy.app.exceptions.UserEmailException;
import com.apipaymybuddy.app.models.Account;
import com.apipaymybuddy.app.models.User;
import com.apipaymybuddy.app.models.UserInfos;
import com.apipaymybuddy.app.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
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
		log.debug("Debut methode findByLastname, arg: lastname ({})", lastname);

		Iterable<User> iterableUser = userRepository.findByLastName(lastname);

		log.debug("Fin methode findByLastname");
		return iterableUser;
	}

	/**
	 * existsByEmail - Checks that a user with this email exists
	 * 
	 * @param mail The e-mail of the user to find
	 * @return true if user with e-mail found, false in other case
	 */
	public boolean existsByEmail(String mail) {
		log.debug("Debut methode existsByEmail, arg: mail ({})", mail);

		boolean status = (userRepository.getNumberUserByEmail(mail) > 0) ? true : false;

		log.debug("Fin methode existsByEmail");
		return status;
	}

	/**
	 * createUser - Add new user and associate account in data base
	 * 
	 * @param user The new user to add
	 * @return operation status, true if success, false in other case
	 */
	public boolean createUser(UserInfos userInfos) {

		log.debug("Debut methode createUser, arg: User ({})", userInfos);
		log.info("Création d'un nouveau User et du Acount associé ({})", userInfos);

		// all attributes must be filled in
		if (atLeastOneAttributeIsEmpty(userInfos)) {

			log.error("Echec opération création User, au moins un des attibuts est vide");
			return false;
		}

		// email attribute must not already exist
		if (existsByEmail(userInfos.getMail())) {
			log.error("Echec opération création User, l'adresse mail existe déjà dans la BDD");
			return false;
		}

		// management rule, these attributes must be capitalized
		User user = new User(); 
		user.setFirstName(userInfos.getFirstName().toUpperCase());
		user.setLastName(userInfos.getLastName().toUpperCase());
		user.setAddress(userInfos.getAddress().toUpperCase());
		user.setCity(userInfos.getCity().toUpperCase());
		user.setPhone(userInfos.getPhone());
		user.setMail(userInfos.getMail());
		user.setPassword(userInfos.getPassword());

		// new account to associate with the new user
		Account account = new Account();
		Date date = new Date(System.currentTimeMillis());
		account.setOpenDate(Date.valueOf(date.toString()));
		account.setSolde(0);
		account.setAccountOwner(user);
		log.info("Opération création Account associé");

		user.setAccountUser(account);

		boolean statusOperation = save(user).equals(user);
		log.info("Opération création User: ({})", statusOperation ? "Succes" : "Echec");
		log.debug("Fin methode createUser");
		return statusOperation;

	}
	
	
	/**
	 * updateUser - Update informations of user in data base
	 * 
	 * @param user The user to update
	 * @return operation status, true if success, false in other case
	 */
	public boolean updateUser(UserInfos userInfos) {

		log.debug("Debut methode createUser, arg: User ({})", userInfos);
		log.info("Création d'un nouveau User et du Acount associé ({})", userInfos);

		// all attributes must be filled in
		if (atLeastOneAttributeIsEmpty(userInfos)) {

			log.error("Echec opération création User, au moins un des attibuts est vide");
			return false;
		}

		// email attribute must not already exist
		Optional<User> optUser = findByMail(userInfos.getMail());
		if (optUser.isEmpty()) {

			log.error("User with Email ({}) does not exist.", userInfos.getMail());
			throw new UserEmailException(userInfos.getMail());
		}


		log.debug("User with Email ({}) exist.", userInfos.getMail());

		// management rule, these attributes must be capitalized
		User user = optUser.get();
		user.setFirstName(userInfos.getFirstName().toUpperCase());
		user.setLastName(userInfos.getLastName().toUpperCase());
		user.setAddress(userInfos.getAddress().toUpperCase());
		user.setCity(userInfos.getCity().toUpperCase());
		user.setPhone(userInfos.getPhone());
		user.setMail(userInfos.getMail());
		user.setPassword(userInfos.getPassword());

		log.info("Opération création Account associé");

		boolean statusOperation = save(user).equals(user);
		log.info("Opération création User: ({})", statusOperation ? "Succes" : "Echec");
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
//	private boolean atLeastOneAttributeIsEmpty(User user) {
	private boolean atLeastOneAttributeIsEmpty(UserInfos user) {

		log.debug("Debut methode atLeastOneAttributeIsEmpty, arg: User ({})", user);
		log.trace("Exécute methode atLeastOneAttributeIsEmpty");
		
		boolean status = (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getAddress().isEmpty()
				|| user.getCity().isEmpty() || user.getPhone().isEmpty() || user.getMail().isEmpty()
				|| user.getPassword().isEmpty());

		log.debug("Fin methode atLeastOneAttributeIsEmpty");
		return status;
	}
	
	/**
	 * findByMail - Get users with mail matching given email
	 * 
	 * @param email The email of the user to find
	 * @return A Iterable list of users found
	 */
	public Optional<User> findByMail(String email) {
		log.debug("Debut methode findByMail, arg: email ({})", email);

		// User user = userRepository.findByMail(email);

		Optional<User> optEntity = userRepository.findByMail(email);
		// Optional<UserModel> optModel = Optional.empty();

		if (optEntity.isEmpty()) {

			log.error("User with Email ({}) does not exist.", email);
			throw new UserEmailException(email);
		}

//		Userl userModel = new UserModel();
//		userModel.setUserId(optEntity.get().getUserId());
//		userModel.setLastName(optEntity.get().getLastName());
//		userModel.setFirstName(optEntity.get().getFirstName());
//		userModel.setAddress(optEntity.get().getAddress());
//		userModel.setCity(optEntity.get().getCity());
//		userModel.setPhone(optEntity.get().getPhone());
//		userModel.setMail(optEntity.get().getMail());
//		userModel.setPassword(optEntity.get().getCity());

		log.debug("Fin methode findByMail");
		return optEntity;
	}

}
