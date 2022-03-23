package com.paymybuddy.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.Transfer;
import com.paymybuddy.app.services.AccountService;

@SpringBootTest
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRelationshipsIT {

	@Autowired
	private AccountService accountService;

	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(accountService).isNotNull();
	}



	@Test
	@Transactional
	@Order(5)
	void createTransferTest() {
		
		//GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account account = optAccount.get();
		
		assertThat(account.getConnections()).isNotNull();
		assertThat(account.getConnections()).hasSize(2);
		Account creditedAccount = account.getConnections().get(0).getAccountUser();
		
		Transfer transfer = new Transfer();
		transfer.setAmount(95.90);
		long now = System.currentTimeMillis();
		Timestamp dateHeure = new Timestamp(now);

		transfer.setDate(dateHeure);
		transfer.setDescription("libelé du transfer");
		transfer.setCreditedAccount(creditedAccount);
		transfer.setDebitedAccount(account);
		account.getTransfers().add(transfer);
		int countTransfers = account.getTransfers().size();

		// WHEN
		Account modifiedAccount = accountService.save(account);
		assertThat(modifiedAccount).isNotNull();

		// THEN

		optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		account = optAccount.get();

		assertThat(account.getAccountId()).isNotNull();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertEquals(account.getOpenDate().toString(), "2021-12-02");
		assertEquals(account.getSolde(), 150.45);

		// verify account owner info
		assertThat(account.getAccountOwner()).isNotNull();
		assertEquals(account.getAccountOwner().getLastName(), "jean");
		assertEquals(account.getAccountOwner().getFirstName(), "durand");
		assertEquals(account.getAccountOwner().getAddress(), "56 impasse des souris");
		assertEquals(account.getAccountOwner().getCity(), "marseille");
		assertEquals(account.getAccountOwner().getPhone(), "0632467802");
		assertEquals(account.getAccountOwner().getMail(), "durand.jean@aol.com");
		assertEquals(account.getAccountOwner().getPassword(), "jean2022");

		// verify account connections info
		assertThat(account.getConnections()).isNotNull();
		assertThat(account.getConnections()).hasSize(2);
		assertEquals(account.getConnections().get(0).getLastName(), "louis");
		assertEquals(account.getConnections().get(0).getFirstName(), "dupont");
		assertEquals(account.getConnections().get(0).getAddress(), "15 rue du rouget");
		assertEquals(account.getConnections().get(0).getCity(), "aix-en-provence");
		assertEquals(account.getConnections().get(0).getPhone(), "0745235889");
		assertEquals(account.getConnections().get(0).getMail(), "dupontlouis@hotmail.fr");
		assertEquals(account.getConnections().get(0).getPassword(), "louis2022");

		assertEquals(account.getConnections().get(1).getLastName(), "alain");
		assertEquals(account.getConnections().get(1).getFirstName(), "Lejeune");
		assertEquals(account.getConnections().get(1).getAddress(), "32 avenue léon bloum");
		assertEquals(account.getConnections().get(1).getCity(), "pertuis");
		assertEquals(account.getConnections().get(1).getPhone(), "0490255633");
		assertEquals(account.getConnections().get(1).getMail(), "alejeune@outlook.com");
		assertEquals(account.getConnections().get(1).getPassword(), "alain2022");

		// verify account transfers info
		assertThat(account.getTransfers()).isNotNull();
		assertThat(account.getTransfers()).hasSize(countTransfers);
		assertEquals(account.getTransfers().get(0).getDate(), Timestamp.valueOf("2022-01-10 00:00:00"));
		assertEquals(account.getTransfers().get(0).getDescription(), "Recharge café");
		assertEquals(account.getTransfers().get(0).getAmount(), 5.25);

		assertEquals(account.getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-10 01:25:00"));
		assertEquals(account.getTransfers().get(1).getDescription(), "Paiement rufus");
		assertEquals(account.getTransfers().get(1).getAmount(), 37.95);

		assertEquals(account.getTransfers().get(2).getDate(), dateHeure);
		assertEquals(account.getTransfers().get(2).getDescription(), "libelé du transfer");
		assertEquals(account.getTransfers().get(2).getAmount(), 95.90);
	}

}
