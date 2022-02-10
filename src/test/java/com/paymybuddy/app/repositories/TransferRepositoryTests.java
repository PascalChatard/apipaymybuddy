package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.app.model.Transfer;
import com.paymybuddy.app.repository.TransferRepository;

@SpringBootTest
class TransferRepositoryTests {

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

}
