package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;

@SpringBootTest
@Sql("account.sql")
class AccountRepositoryIT {

	@Autowired
	private AccountRepository accountRepository;


	@Test
	void injectedComponentIsNotNull() {
		assertThat(accountRepository).isNotNull();
	}

	@Test
	void testFetchAllData() {
		Iterable<Account> accounts = accountRepository.findAll();
		// check if there are records
		assertThat(accounts).doesNotContainNull();
		assertThat(accounts).size().isGreaterThan(0);
		assertThat(accounts).size().isEqualTo(3);
	}

	@Test
	void testFetchRecord() {

		// check that exist record with ID's 1
		Optional<Account> optAccount = accountRepository.findById(1);
		assertThat(optAccount).isNotEmpty();

		// check attibuts values
		Account account = optAccount.get();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertThat(account.getOpenDate()).isEqualTo("2021-12-02");
		assertThat(account.getSolde()).isEqualTo(150.45);
	}

	@Test
	void testRecordData() {

		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setOpenDate(date);
		account.setSolde(150.85);

		// check there is no Id before recording data
		assertThat(account.getAccountId()).isNull();
		Account savedAccount = accountRepository.save(account);
		// check there is Id after recording data
		assertThat(savedAccount).isEqualTo(account);
	}

	@Test
	void testDeleteRecordById() {

		assertDoesNotThrow(() -> accountRepository.deleteById(1));
		assertThat(accountRepository.existsById(1)).isFalse();

		// assertThatCode(() ->
		// userRepository.deleteById(1)).doesNotThrowAnyException();
		// assertThatNoException().isThrownBy(userRepository.deleteById(1));
	}

	@Test
	void testDeleteRecordById_ThrowEmptyResultDataAccessException() {

		// check that delete a record with a ID out of bound throws an
		// exception
		assertThatExceptionOfType(EmptyResultDataAccessException.class)
				.isThrownBy(() -> accountRepository.deleteById(1000));
	}

	@Test
	void testDeleteRecord() {

		// check first record
		Optional<Account> optAccount = accountRepository.findById(1);
		Account account = optAccount.get();
		assertThat(account).isNotNull();
		accountRepository.delete(account);
		// check that get optional with finding record with ID 1 throws an exception
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> accountRepository.findById(1).get());
		// check that finding record with ID 1 return empty optional
		assertThat(accountRepository.findById(1)).isEmpty();
	}

	@Test
	void testCountData() {
		// there are three records in database
		assertThat(accountRepository.count()).isEqualTo(3);
	}

	@Test
	void testExistById_True() {
		// there are three records in database and ID's are (1,2,3)
		assertThat(accountRepository.existsById(1)).isTrue();
	}

	@Test
	void testExistById_False() {
		// there are three records in database and ID's are (1,2,3)
		assertThat(accountRepository.existsById(100)).isFalse();
	}

}
