package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;

@SpringBootTest
@Sql("account.sql")
class AccountRepositoryIT {

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

	@Test
	void testRecordData() {

		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setOpenDate(date);
		account.setSolde(150.85);

		// check there is no Id before recording data
		assertNull(account.getAccountId());
		accountRepository.save(account);
		// check there is Id after recording data
		assertNotNull(account.getAccountId());
	}

}
