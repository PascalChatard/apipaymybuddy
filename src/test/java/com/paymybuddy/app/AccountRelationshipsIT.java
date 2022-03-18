package com.paymybuddy.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.User;
import com.paymybuddy.app.services.AccountService;
import com.paymybuddy.app.services.UserService;

@SpringBootTest
@Sql("data.sql")
class AccountRelationshipsIT {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Test
	void injectedComponentIsNotNull() {
		assertThat(accountService).isNotNull();
	}

	@Test
	void accessUserAndTransferDataFromAccountEntityTest() {

		// WHEN
		Optional<Account> optAccount = accountService.findById(1);

		// THEN
		assertThat(optAccount).isNotEmpty();
		Account account = optAccount.get();
		assertThat(account).isNotNull();
		assertThat(account.getAccountId()).isNotNull();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertEquals(account.getOpenDate().toString(), "2021-12-02");
		assertEquals(account.getSolde(), 150.45);

		assertThat(account.getAccountOwner()).isNotNull();
		assertEquals(account.getAccountOwner().getLastName(), "jean");
		assertEquals(account.getAccountOwner().getFirstName(), "durand");
		assertEquals(account.getAccountOwner().getAddress(), "56 impasse des souris");
		assertEquals(account.getAccountOwner().getCity(), "marseille");
		assertEquals(account.getAccountOwner().getPhone(), "0632467802");
		assertEquals(account.getAccountOwner().getMail(), "durand.jean@aol.com");
		assertEquals(account.getAccountOwner().getPassword(), "jean2022");

		assertThat(account.getTransfers()).isNotNull();
		assertEquals(account.getTransfers().get(0).getDate(), Timestamp.valueOf("2022-01-10 00:00:00"));
		assertEquals(account.getTransfers().get(0).getDescription(), "Recharge café");
		assertEquals(account.getTransfers().get(0).getAmount(), 5.25);

		assertEquals(account.getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-10 01:25:00"));
		assertEquals(account.getTransfers().get(1).getDescription(), "Paiement rufus");
		assertEquals(account.getTransfers().get(1).getAmount(), 37.95);

	}

	@Test
	void accessAccountAndTransferDataFromUserEntityTest() {

		// WHEN
		Optional<User> optUser = userService.findById(2);

		// THEN
		assertThat(optUser).isNotEmpty();
		User user = optUser.get();
		// user informations
		assertEquals(user.getLastName(), "louis");
		assertEquals(user.getFirstName(), "dupont");
		assertEquals(user.getAddress(), "15 rue du rouget");
		assertEquals(user.getCity(), "aix-en-provence");
		assertEquals(user.getPhone(), "0745235889");
		assertEquals(user.getMail(), "dupontlouis@hotmail.fr");
		assertEquals(user.getPassword(), "louis2022");
		// account informations
		assertThat(user.getAccountUser()).isNotNull();
		assertThat(user.getAccountUser().getAccountId()).isEqualTo(2);
		assertThat(user.getAccountUser().getOpenDate()).isEqualTo(Date.valueOf("2021-11-22"));
		assertThat(user.getAccountUser().getSolde()).isEqualTo(25.50);
		// transfer informations
		assertThat(user.getAccountUser().getTransfers()).isNotNull();
		assertThat(user.getAccountUser().getTransfers().size()).isEqualTo(2);

		assertEquals(user.getAccountUser().getTransfers().get(0).getDate(), Timestamp.valueOf("2022-01-02 00:00:00"));
		assertEquals(user.getAccountUser().getTransfers().get(0).getDescription(), "Remboursement ciné");
		assertEquals(user.getAccountUser().getTransfers().get(0).getAmount(), 15.05);

		assertEquals(user.getAccountUser().getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-02 00:00:00"));
		assertEquals(user.getAccountUser().getTransfers().get(1).getDescription(), "Participation cadeau");
		assertEquals(user.getAccountUser().getTransfers().get(1).getAmount(), 5.50);

	}

}
