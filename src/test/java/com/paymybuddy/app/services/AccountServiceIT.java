package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;

@SpringBootTest
@Sql("account.sql")
class AccountServiceIT {

	@Autowired
	AccountService accountService;

	@Test
	void injectedComponentIsNotNull() {
		assertThat(accountService).isNotNull();
	}

	@Test
	void testFetchRecord() {

		// WHEN
		Optional<Account> optAccount = accountService.findById(1);

		// THEN
		assertThat(optAccount).isNotEmpty();
		Account account = optAccount.get();
		assertThat(account).isNotNull();
		assertThat(account.getAccountId()).isNotNull();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertEquals(account.getOpenDate().toString(), "2021-12-02");
		assertEquals(account.getSolde(), 150.45);
	}

	@Test
	void testFetchAllRecord() {

		// WHEN
		Iterable<Account> accounts = accountService.findAll();

		// THEN
		// check if there are records
		assertThat(accounts).doesNotContainNull();
		assertThat(accounts).size().isGreaterThan(0);
		assertThat(accounts).size().isEqualTo(3L);
	}

	@Test
	void testSaveEntity() {

		// GIVEN
		Date date = Date.valueOf("2022-01-23");

		Account account = new Account();
		account.setOpenDate(date);
		account.setSolde(150.85);

		// WHEN
		Account savedAccount = accountService.save(account);

		// THEN
		assertThat(savedAccount).isNotNull();
		assertThat(savedAccount).isEqualTo(account);
	}

	@Test
	void testDeleteRecordById() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account Account = optAccount.get();

		// WHEN
		accountService.deleteById(Account.getAccountId());

		// THEN
		assertThat(accountService.existsById(1)).isFalse();
	}

	@Test
	void testDeleteRecordByEntity() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account Account = optAccount.get();

		// WHEN
		accountService.delete(Account);

		// THEN
		assertThat(accountService.existsById(Account.getAccountId())).isFalse();
	}

	@Test
	void testCountRecords() {

		// WHEN
		long nbRecords = accountService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3L);
	}

	@Test
	void testExistById_True() {

		// WHEN
		boolean existUser = accountService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	void testExistById_False() {

		// WHEN
		boolean existUser = accountService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}


}
