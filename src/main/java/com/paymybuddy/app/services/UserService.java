package com.paymybuddy.app.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.User;
import com.paymybuddy.app.repositories.UserRepository;

@Service
public class UserService extends GenericService<User> {

	@Autowired
	UserRepository userRepository;

	public Iterable<User> findByLastname(String lastname) {
		return userRepository.findByLastName(lastname);
	}

	public boolean existsByEmail(String mail) {
		return (userRepository.existsByEmail(mail) > 0) ? true : false;
	}

	public boolean createUser(User user) {

		// all attributes must be filled in
		if (atLeastOneAttributeIsEmpty(user)) {
			return false;
		}

		// email attribute must not already exist
		if (existsByEmail(user.getMail())) {
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

		user.setAccountUser(account);

		return save(user).equals(user);
	}

	private boolean atLeastOneAttributeIsEmpty(User user) {
		return (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getAddress().isEmpty()
				|| user.getCity().isEmpty() || user.getPhone().isEmpty() || user.getMail().isEmpty()
				|| user.getPassword().isEmpty());
	}

}
