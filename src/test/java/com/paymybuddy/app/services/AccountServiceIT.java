package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;

@SpringBootTest
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceIT {

	@Autowired
	AccountService accountService;

	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(accountService).isNotNull();
	}

	@Test
	@Order(2)
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
	@Order(3)
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
	@Order(4)
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
	@Order(5)
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
	@Order(6)
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
	@Order(7)
	void testCountRecords() {

		// WHEN
		long nbRecords = accountService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3L);
	}

	@Test
	@Order(8)
	void testExistById_True() {

		// WHEN
		boolean existUser = accountService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	@Order(9)
	void testExistById_False() {

		// WHEN
		boolean existUser = accountService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}


}
