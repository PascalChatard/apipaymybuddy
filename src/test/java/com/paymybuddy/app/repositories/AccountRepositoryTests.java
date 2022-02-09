package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.app.model.Account;
import com.paymybuddy.app.repository.AccountRepository;

@SpringBootTest
class AccountRepositoryTests {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	final void test() {
		assertTrue(true);
	}

	@Test
	void injectedComponentIsNotNull() {
		assertThat(accountRepository).isNotNull();
	}

	@Test
	void testFetchData() {
		Iterable<Account> accounts = accountRepository.findAll();
		// check if there are records
		assertNotNull(accounts);
		int count = 0;
		for (Account p : accounts) {
			count++;
		}
		// there are three records in database
		assertEquals(count, 3);

		// check first record
		Optional<Account> optAccount = accountRepository.findById(1);
		Account account = optAccount.get();
		assertThat(account).isNotNull();
		assertNotNull(account.getAccountId());
		assertThat(account.getOpenDate()).isEqualTo("2021-12-02");
		assertEquals(account.getSolde(), 150.45);

	}

}
