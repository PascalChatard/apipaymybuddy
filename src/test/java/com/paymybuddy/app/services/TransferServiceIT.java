package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Transfer;

@SpringBootTest
@Sql("transfer.sql")
class TransferServiceIT {

	@Autowired
	TransferService transferService;


	@Test
	void injectedComponentIsNotNull() {
		assertThat(transferService).isNotNull();
	}


	@Test
	void testFetchRecord() {

		// WHEN
		Optional<Transfer> optTransfer = transferService.findById(1);

		// THEN
		assertThat(optTransfer).isNotEmpty();
		Transfer transfer = optTransfer.get();
		assertThat(transfer).isNotNull();
		assertThat(transfer.getTransferId()).isNotNull();
		assertThat(transfer.getTransferId()).isEqualTo(1);
		assertEquals(transfer.getDate().toString(), "2022-01-02");
		assertEquals(transfer.getAmount(), 15.05);
		assertEquals(transfer.getDescription(), "Remboursement ciné");
	}


	@Test
	void testFetchAllRecord() {

		// WHEN
		Iterable<Transfer> transfers = transferService.findAll();

		// THEN
		// check if there are records
		assertThat(transfers).doesNotContainNull();
		assertThat(transfers).size().isGreaterThan(0);
		assertThat(transfers).size().isEqualTo(6);
	}


	@Test
	void testSaveEntity() {

		// GIVEN
		Date date = Date.valueOf("2021-08-02");

		Transfer transfer = new Transfer();
		transfer.setDate(date);
		transfer.setDescription("Remboursement de qqch");
		transfer.setAmount(11.55);

		// WHEN
		Transfer savedTransfer = transferService.save(transfer);

		// THEN
		assertThat(savedTransfer).isNotNull();
		assertThat(savedTransfer).isEqualTo(transfer);
	}


	@Test
	void testDeleteRecordById() {

		// GIVEN
		Optional<Transfer> optTransfer = transferService.findById(1);
		assertThat(optTransfer).isNotEmpty();
		Transfer transfer = optTransfer.get();

		// WHEN
		transferService.deleteById(transfer.getTransferId());

		// THEN
		assertThat(transferService.existsById(1)).isFalse();
	}


	@Test
	void testDeleteRecordByEntity() {

		// GIVEN
		Optional<Transfer> optTransfer = transferService.findById(1);
		assertThat(optTransfer).isNotEmpty();
		Transfer transfer = optTransfer.get();

		// WHEN
		transferService.delete(transfer);

		// THEN
		assertThat(transferService.existsById(transfer.getTransferId())).isFalse();
	}

	@Test
	void testCountRecords() {

		// WHEN
		long nbRecords = transferService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(6L);
	}

	@Test
	void testExistById_True() {

		// WHEN
		boolean existUser = transferService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	void testExistById_False() {

		// WHEN
		boolean existUser = transferService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}

}
