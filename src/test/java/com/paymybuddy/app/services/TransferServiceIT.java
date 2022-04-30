package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.Timestamp;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Transfer;

@ActiveProfiles("dev")
@SpringBootTest
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransferServiceIT {

	@Autowired
	TransferService transferService;


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(transferService).isNotNull();
	}


	@Test
	@Order(2)
	void testFetchRecord() {

		// WHEN
		Optional<Transfer> optTransfer = transferService.findById(1);

		// THEN
		assertThat(optTransfer).isNotEmpty();
		Transfer transfer = optTransfer.get();
		assertThat(transfer).isNotNull();
		assertThat(transfer.getTransferId()).isNotNull();
		assertThat(transfer.getTransferId()).isEqualTo(1);
		assertThat(transfer.getDate()).isEqualTo(Timestamp.valueOf("2022-01-02 00:00:00"));

		assertThat(transfer.getAmount()).isEqualTo(15.05);
		assertThat(transfer.getDescription()).isEqualTo("Remboursement ciné");
	}


	@Test
	@Order(3)
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
	@Order(4)
	void testSaveEntity() {

		// GIVEN
		Timestamp date = Timestamp.valueOf("2021-02-08 00:00:00");

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
	@Transactional
	@Order(5)
	void testDeleteRecordById() {

		// GIVEN
		Optional<Transfer> optTransfer = transferService.findById(1);
		assertThat(optTransfer).isNotEmpty();
		Transfer transfer = optTransfer.get();

		// WHEN
		assertDoesNotThrow(() -> transferService.deleteById(transfer.getTransferId()));

		// THEN
		assertThat(transferService.findById(1)).isEmpty();
	}


	@Test
	@Transactional
	@Order(6)
	void testDeleteRecordByEntity() {

		// GIVEN
		Optional<Transfer> optTransfer = transferService.findById(1);
		assertThat(optTransfer).isNotEmpty();
		Transfer transfer = optTransfer.get();

		// WHEN
		transferService.delete(transfer);

		// THEN
		assertThat(transferService.findById(1)).isEmpty();
	}

	@Test
	@Order(7)
	void testCountRecords() {

		// WHEN
		long nbRecords = transferService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(6L);
	}

	@Test
	@Order(8)
	void testExistById_True() {

		// WHEN
		boolean existUser = transferService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	@Order(9)
	void testExistById_False() {

		// WHEN
		boolean existUser = transferService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}

}
