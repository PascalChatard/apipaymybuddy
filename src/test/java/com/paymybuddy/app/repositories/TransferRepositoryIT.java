package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Transfer;

@SpringBootTest
@Sql("transfer.sql")
class TransferRepositoryIT {

	@Autowired
	private TransferRepository transferRepository;


	@Test
	void injectedComponentIsNotNull() {

		// THEN
		assertThat(transferRepository).isNotNull();
	}

	@Test
	void testFetchAllData() {

		// WHEN
		Iterable<Transfer> transfers = transferRepository.findAll();

		// THEN
		// check if there are records
		assertThat(transfers).doesNotContainNull();
		assertThat(transfers).size().isGreaterThan(0);
		assertThat(transfers).size().isEqualTo(6);
	}

	@Test
	void testFetchRecord() {

		// WHEN
		// check that exist record with ID's 1
		Optional<Transfer> optTransfer = transferRepository.findById(1);
		assertThat(optTransfer).isNotEmpty();

		// THEN
		// check attibuts values
		Transfer transfer = optTransfer.get();
		assertThat(transfer.getTransferId()).isEqualTo(1);
		assertThat(transfer.getDate()).isEqualTo("2022-01-02");
		assertEquals(transfer.getDescription(), "Remboursement ciné");
		assertEquals(transfer.getAmount(), 15.05);
	}

	@Test
	void testRecordData() {

		// GIVEN
		Date date = Date.valueOf("2022-01-02");
		Transfer transfer = new Transfer();
		transfer.setDate(date);
		transfer.setDescription("Remboursement de qqch");
		transfer.setAmount(11.55);

		// WHEN
		Transfer savedTransfer = transferRepository.save(transfer);

		// THEN
		assertThat(savedTransfer).isEqualTo(transfer);
	}

	@Test
	void testDeleteRecordById() {

		// THEN
		assertDoesNotThrow(() -> transferRepository.deleteById(1));
		assertThat(transferRepository.existsById(1)).isFalse();
	}

	@Test
	void testDeleteRecordById_ThrowEmptyResultDataAccessException() {

		// THEN
		// check that delete a record with a ID out of bound throws an
		// exception
		assertThatExceptionOfType(EmptyResultDataAccessException.class)
				.isThrownBy(() -> transferRepository.deleteById(1000));
	}

	@Test
	void testDeleteRecord() {

		// GIVEN
		// check first record
		Optional<Transfer> optTransfer = transferRepository.findById(1);
		Transfer transfer = optTransfer.get();
		assertThat(transfer).isNotNull();

		// WHEN
		transferRepository.delete(transfer);

		// THEN
		// check that get optional with finding record with ID 1 throws an exception
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> transferRepository.findById(1).get());
		// check that finding record with ID 1 return empty optional
		assertThat(transferRepository.findById(1)).isEmpty();
	}

	@Test
	void testCountData() {

		// THEN
		// there are three records in database
		assertThat(transferRepository.count()).isEqualTo(6);
	}

	@Test
	void testExistById_True() {

		// THEN
		// there are three records in database and ID's are (1,2,3)
		assertThat(transferRepository.existsById(1)).isTrue();
	}

	@Test
	void testExistById_False() {

		// THEN
		// there are three records in database and ID's are (1,2,3)
		assertThat(transferRepository.existsById(100)).isFalse();
	}
}
