package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.repositories.AccountRepository;


@ActiveProfiles("dev")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceTest {

	@Mock
	private AccountRepository mockRepository;

	private AutoCloseable closeable;

	@InjectMocks
	AccountService accountService;

	Account account;
	Iterable<Account> accounts;

	@BeforeEach
	void setUp() throws Exception {

		closeable = MockitoAnnotations.openMocks(this);

		Date date = Date.valueOf("2022-01-23");
		account = new Account();
		account.setAccountId(1);
		account.setOpenDate(date);
		account.setSolde(150.85);

		accounts = Arrays.asList(account);
	}

	@AfterEach
	void tearDown() throws Exception {

		closeable.close();
	}


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(mockRepository).isNotNull();
		assertThat(accountService).isNotNull();
	}


	@Test
	@Order(2)
	void testFetchAllData() {
		// GIVEN
		when(mockRepository.findAll()).thenReturn(accounts);

		// WHEN
		Iterable<Account> accountsRead = accountService.findAll();

		// THEN
		// check if there are records
		assertThat(accountsRead).doesNotContainNull();
		assertThat(accountsRead).size().isGreaterThan(0);
		assertThat(accountsRead).size().isEqualTo(1);
		verify(mockRepository, times(1)).findAll();
		verifyNoMoreInteractions(mockRepository);
	}


	@Order(3)
	@Test
	void testFetchRecord() {

		// GIVEN
		when(mockRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(account));

		// WHEN
		Optional<Account> optAccount = accountService.findById(1);

		// THEN
		assertThat(optAccount).isNotEmpty();
		Account accountFinded = optAccount.get();
		assertThat(accountFinded).isEqualTo(account);
		verify(mockRepository, times(1)).findById(1);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	@Order(4)
	void testDeleteRecordById() {

		// GIVEN
		doNothing().doThrow(new RuntimeException()).when(mockRepository).deleteById(ArgumentMatchers.anyInt());

		// WHEN
		accountService.deleteById(1);

		// THEN
		verify(mockRepository, times(1)).deleteById(1);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	@Order(5)
	void testDeleteRecord() {

		// GIVEN
		doNothing().doThrow(new RuntimeException()).when(mockRepository).delete(ArgumentMatchers.any(Account.class));

		// WHEN
		accountService.delete(account);

		// THEN
		verify(mockRepository, times(1)).delete(account);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	@Order(6)
	void testCountData() {

		// GIVEN
		when(mockRepository.count()).thenReturn(3L);

		// WHEN
		long nbRecords = accountService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3);
		verify(mockRepository, times(1)).count();
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	@Order(7)
	void testExistById_True() {

		// GIVEN
		when(mockRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(true);

		// WHEN
		boolean existUser = accountService.existsById(15);

		// THEN
		assertThat(existUser).isTrue();
		verify(mockRepository, times(1)).existsById(15);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	@Order(8)
	void testExistById_False() {
		// GIVEN
		when(mockRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(false);

		// WHEN
		boolean existUser = accountService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
		verify(mockRepository, times(1)).existsById(15);
		verifyNoMoreInteractions(mockRepository);
	}

}
