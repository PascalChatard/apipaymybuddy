package com.paymybuddy.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.User;
import com.paymybuddy.app.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	Optional<User> findById(Integer userId) {
		return userRepository.findById(userId);
	}

	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public void deleteById(Integer userId) {
		userRepository.deleteById(userId);
	}

	public void delete(User user) {
		userRepository.delete(user);
	}

	public boolean existsById(Integer userId) {
		return userRepository.existsById(userId);
	}

}
