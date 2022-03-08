package com.paymybuddy.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.repositories.AccountRepository;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	public Optional<Account> findById(Integer accountId) {
		return accountRepository.findById(accountId);
	}

	public Iterable<Account> findAll() {
		return accountRepository.findAll();
	}

	public Account save(Account user) {
		return accountRepository.save(user);
	}

	public void deleteById(Integer accountId) {
		accountRepository.deleteById(accountId);
	}

	public void delete(Account user) {
		accountRepository.delete(user);
	}

	public boolean existsById(Integer accountId) {
		return accountRepository.existsById(accountId);
	}

	public long count() {
		return accountRepository.count();
	}

}
