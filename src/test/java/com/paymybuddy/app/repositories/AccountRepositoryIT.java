package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.User;

@SpringBootTest
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRepositoryIT {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {

		// THEN
		assertThat(accountRepository).isNotNull();
	}

	@Test
	@Order(2)
	void testFetchAllData() {

		// WHEN
		Iterable<Account> accounts = accountRepository.findAll();

		// THEN
		// check if there are records
		assertThat(accounts).doesNotContainNull();
		assertThat(accounts).size().isGreaterThan(0);
		assertThat(accounts).size().isEqualTo(3);
	}

	@Test
	@Order(3)
	void testFetchRecord() {

		// WHEN
		// check that exist record with ID's 1
		Optional<Account> optAccount = accountRepository.findById(1);
		assertThat(optAccount).isNotEmpty();

		// THEN
		// check attibuts values
		Account account = optAccount.get();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertThat(account.getOpenDate()).isEqualTo("2021-12-02");
		assertThat(account.getSolde()).isEqualTo(150.45);
	}

	@Test
	@Order(4)
	void testRecordData() {

		// GIVEN
		Optional<User> optUser = userRepository.findById(4);
		assertThat(optUser).isNotEmpty();
		User user = optUser.get();

		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setOpenDate(date);
		account.setSolde(150.85);
		account.setAccountOwner(user);

		// WHEN
		Account savedAccount = accountRepository.save(account);

		// THEN
		assertThat(savedAccount).isEqualTo(account);
	}

	@Test
	@Order(5)
	void testDeleteRecordById() {

		// THEN
		assertDoesNotThrow(() -> accountRepository.deleteById(1));
		assertThat(accountRepository.existsById(1)).isFalse();
	}

	@Test
	@Order(6)
	void testDeleteRecordById_ThrowEmptyResultDataAccessException() {

		// THEN
		// check that delete a record with a ID out of bound throws an
		// exception
		assertThatExceptionOfType(EmptyResultDataAccessException.class)
				.isThrownBy(() -> accountRepository.deleteById(1000));
	}

	@Test
	@Order(7)
	void testDeleteRecord() {

		// GIVEN
		// check first record
		Optional<Account> optAccount = accountRepository.findById(1);
		Account account = optAccount.get();
		assertThat(account).isNotNull();

		// WHEN
		accountRepository.delete(account);

		// THEN
		// check that get optional with finding record with ID 1 throws an exception
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> accountRepository.findById(1).get());
		// check that finding record with ID 1 return empty optional
		assertThat(accountRepository.findById(1)).isEmpty();
	}

	@Test
	@Order(8)
	void testCountData() {

		// THEN
		// there are three records in database
		assertThat(accountRepository.count()).isEqualTo(3);
	}

	@Test
	@Order(9)
	void testExistById_True() {

		// THEN
		// there are three records in database and ID's are (1,2,3)
		assertThat(accountRepository.existsById(1)).isTrue();
	}

	@Test
	@Order(10)
	void testExistById_False() {

		// THEN
		// there are three records in database and ID's are (1,2,3)
		assertThat(accountRepository.existsById(100)).isFalse();
	}

}
