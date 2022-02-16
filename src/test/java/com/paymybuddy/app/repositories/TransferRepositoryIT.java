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

import com.paymybuddy.app.models.Transfer;

@SpringBootTest
@Sql("transfer.sql")
class TransferRepositoryIT {

	@Autowired
	private TransferRepository transferRepository;

	@Test
	final void test() {
		assertTrue(true);
	}

	@Test
	void injectedComponentIsNotNull() {
		assertThat(transferRepository).isNotNull();
	}

	@Test
	void testFetchData() {
		Iterable<Transfer> transfers = transferRepository.findAll();
		// check if there are records
		assertNotNull(transfers);
		int count = 0;
		for (Transfer p : transfers) {
			count++;
		}
		// there are three records in database
		assertEquals(6, count);

		// check first record
		Optional<Transfer> optTransfer = transferRepository.findById(1);
		Transfer transfer = optTransfer.get();
		assertThat(transfer).isNotNull();
		assertNotNull(transfer.getTransferId());
		assertThat(transfer.getDate()).isEqualTo("2022-01-02");
		assertEquals(transfer.getDescription(), "Remboursement ciné");
		assertEquals(transfer.getAmount(), 15.05);
	}

	@Test
	void testRecordData() {

		Date date = Date.valueOf("2022-01-02");
		Transfer transfer = new Transfer();
		transfer.setDate(date);
		transfer.setDescription("Remboursement de qqch");
		transfer.setAmount(11.55);

		// check there is no Id before recording data
		assertNull(transfer.getTransferId());
		transferRepository.save(transfer);
		// check there is Id after recording data
		assertNotNull(transfer.getTransferId());
	}

}
