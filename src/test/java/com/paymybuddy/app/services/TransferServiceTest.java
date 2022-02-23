package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.app.models.Transfer;
import com.paymybuddy.app.repositories.TransferRepository;

@SpringBootTest
class TransferServiceTest {

	@Mock
	private TransferRepository mockRepository;
	private AutoCloseable closeable;

	@InjectMocks
	TransferService transferService;

	Transfer transfer;
	Iterable<Transfer> transfers;


	@BeforeEach
	void setUp() throws Exception {

		closeable = MockitoAnnotations.openMocks(this);
		Date date = Date.valueOf("2021-08-02");
		transfer = new Transfer();
		transfer.setTransferId(1);
		transfer.setDate(date);
		transfer.setDescription("Remboursement de qqch");
		transfer.setAmount(11.55);

		transfers = Arrays.asList(transfer);
	}

	@AfterEach
	void tearDown() throws Exception {

		closeable.close();
	}


	@Test
	void injectedComponentIsNotNull() {
		assertThat(mockRepository).isNotNull();
		assertThat(transferService).isNotNull();
	}


	@Test
	void testFetchAllData() {
		// GIVEN
		when(mockRepository.findAll()).thenReturn(transfers);

		// WHEN
		Iterable<Transfer> transfersRead = transferService.findAll();

		// THEN
		// check if there are records
		assertThat(transfersRead).doesNotContainNull();
		assertThat(transfersRead).size().isGreaterThan(0);
		assertThat(transfersRead).size().isEqualTo(1);
		verify(mockRepository, times(1)).findAll();
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testFetchRecord() {

		// GIVEN
		when(mockRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(transfer));

		// WHEN
		Optional<Transfer> optTransfer = transferService.findById(1);

		// THEN
		assertThat(optTransfer).isNotEmpty();
		Transfer transferFinded = optTransfer.get();
		assertThat(transferFinded).isEqualTo(transfer);
		verify(mockRepository, times(1)).findById(1);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testRecordData() {

		// GIVEN
		when(mockRepository.save(ArgumentMatchers.any(Transfer.class))).thenReturn(transfer);

		// WHEN
		Transfer savedTransfer = transferService.save(transfer);

		// THEN
		assertThat(savedTransfer).isEqualTo(transfer);
		verify(mockRepository, times(1)).save(transfer);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testDeleteRecordById() {

		// WHEN
		transferService.deleteById(1);

		// THEN
		verify(mockRepository, times(1)).deleteById(1);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testDeleteRecord() {

		// WHEN
		transferService.delete(transfer);

		// THEN
		verify(mockRepository, times(1)).delete(transfer);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testCountData() {

		// GIVEN
		when(mockRepository.count()).thenReturn(3L);

		// WHEN
		long nbRecords = transferService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3);
		verify(mockRepository, times(1)).count();
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testExistById_True() {

		// GIVEN
		when(mockRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(true);

		// WHEN
		boolean existUser = transferService.existsById(15);

		// THEN
		assertThat(existUser).isTrue();
		verify(mockRepository, times(1)).existsById(15);
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testExistById_False() {
		// GIVEN
		when(mockRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(false);

		// WHEN
		boolean existUser = transferService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
		verify(mockRepository, times(1)).existsById(15);
		verifyNoMoreInteractions(mockRepository);
	}

}
