package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
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

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.Rate;
import com.paymybuddy.app.models.User;

@ActiveProfiles("dev")
@SpringBootTest
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceIT {

	@Autowired
	AccountService accountService;

	@Autowired
	RateService rateService;

	@Autowired
	UserService userService;


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(accountService).isNotNull();
		assertThat(rateService).isNotNull();
	}

	@Test
	@Order(2)
	void testFetchAccountRecord() {

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
	}

	@Test
	@Order(3)
	void testFetchAllAccountRecord() {

		// WHEN
		Iterable<Account> accounts = accountService.findAll();

		// THEN
		// check if there are records
		assertThat(accounts).doesNotContainNull();
		assertThat(accounts).size().isGreaterThan(0);
		assertThat(accounts).size().isEqualTo(3L);
	}

	@Test
	@Order(4)
	void testSaveAccountEntity() {

		// GIVEN
		Date date = Date.valueOf("2022-01-23");

		Account account = new Account();
		account.setOpenDate(date);
		account.setSolde(150.85);

		// WHEN
		Account savedAccount = accountService.save(account);

		// THEN
		assertThat(savedAccount).isNotNull();
		assertThat(savedAccount).isEqualTo(account);
	}

	@Test
	@Order(5)
	void testDeleteAccountRecordById() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account Account = optAccount.get();

		// WHEN
		accountService.deleteById(Account.getAccountId());

		// THEN
		assertThat(accountService.existsById(1)).isFalse();
	}

	@Test
	@Order(6)
	void testDeleteAccountRecordByEntity() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account Account = optAccount.get();

		// WHEN
		accountService.delete(Account);

		// THEN
		assertThat(accountService.existsById(Account.getAccountId())).isFalse();
	}

	@Test
	@Order(7)
	void testCountAccountRecords() {

		// WHEN
		long nbRecords = accountService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3L);
	}

	@Test
	@Order(8)
	void testExistAccountById_True() {

		// WHEN
		boolean existUser = accountService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	@Order(9)
	void testExistAccountById_False() {

		// WHEN
		boolean existUser = accountService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}

	@Test
	@Order(10)
	void accessUserConnectionsAndTransfersDataFromAccountEntityTest() {

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
		assertThat(account.getTransfers()).hasSize(2);

		assertEquals(account.getTransfers().get(0).getDate(), Timestamp.valueOf("2022-01-10 00:00:00"));
		assertEquals(account.getTransfers().get(0).getDescription(), "Recharge café");
		assertEquals(account.getTransfers().get(0).getAmount(), 5.25);
		assertThat(account.getTransfers().get(0).getCreditedAccount().getAccountId()).isEqualTo(2);
		assertThat(account.getTransfers().get(0).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(0).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(0).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(0).getCost()).isEqualTo(0.03);

		assertEquals(account.getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-10 01:25:00"));
		assertEquals(account.getTransfers().get(1).getDescription(), "Paiement rufus");
		assertEquals(account.getTransfers().get(1).getAmount(), 37.95);
		assertThat(account.getTransfers().get(1).getCreditedAccount().getAccountId()).isEqualTo(3);
		assertThat(account.getTransfers().get(1).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(1).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(1).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(1).getCost()).isEqualTo(0.19);

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

	}

	@Test
	@Transactional
	@Order(11)
	void makeTransferFromAccountEntityTest() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account account = optAccount.get();

		assertThat(account.getConnections()).isNotNull();
		assertThat(account.getConnections()).hasSize(2);
		User beneficiaryUser = account.getConnections().get(0);

		int countTransfers = account.getTransfers().size();

		// truncate the fractional seconds component
		Timestamp dateHeure = truncateFractionalSecondsComponent(new Timestamp(System.currentTimeMillis()));

		Optional<Rate> optRate = rateService.findById(1);
		assertThat(optRate).isNotEmpty();
		Rate rate = optRate.get();

		// WHEN
		Account modifiedAccount = accountService.makeTransfer(account, beneficiaryUser, "Description du Transfer",
				95.90, rate);
		assertThat(modifiedAccount).isNotNull();

		// THEN
		optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		account = optAccount.get();

		assertThat(account.getAccountId()).isNotNull();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertEquals(account.getOpenDate().toString(), "2021-12-02");
		assertThat(account.getSolde()).isEqualTo(54.55);

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
		assertThat(account.getTransfers()).hasSize(countTransfers + 1);
		assertEquals(account.getTransfers().get(0).getDate(), Timestamp.valueOf("2022-01-10 00:00:00"));
		assertEquals(account.getTransfers().get(0).getDescription(), "Recharge café");
		assertEquals(account.getTransfers().get(0).getAmount(), 5.25);
		assertThat(account.getTransfers().get(0).getCreditedAccount().getAccountId()).isEqualTo(2);
		assertThat(account.getTransfers().get(0).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(0).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(0).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(0).getCost()).isEqualTo(0.03);

		assertEquals(account.getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-10 01:25:00"));
		assertEquals(account.getTransfers().get(1).getDescription(), "Paiement rufus");
		assertEquals(account.getTransfers().get(1).getAmount(), 37.95);
		assertThat(account.getTransfers().get(1).getCreditedAccount().getAccountId()).isEqualTo(3);
		assertThat(account.getTransfers().get(1).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(1).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(1).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(1).getCost()).isEqualTo(0.19);

		assertThat(truncateFractionalSecondsComponent(account.getTransfers().get(2).getDate())).isEqualTo(dateHeure);
		assertEquals(account.getTransfers().get(2).getDescription(), "Description du Transfer");
		assertEquals(account.getTransfers().get(2).getAmount(), 95.90);
		assertThat(account.getTransfers().get(2).getCreditedAccount().getAccountId())
				.isEqualTo(beneficiaryUser.getAccountUser().getAccountId());
		assertThat(account.getTransfers().get(2).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(2).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(2).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(2).getCost()).isEqualTo(0.48);

		// verify solde of credited account
		assertThat(beneficiaryUser.getAccountUser().getSolde()).isEqualTo(121.40);

	}

	@Test
	@Transactional
	@Order(12)
	void makeTransferFromAccountEntity_WithoutSuccesTest_WhenNegativeAmount() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account account = optAccount.get();

		assertThat(account.getConnections()).isNotNull();
		assertThat(account.getConnections()).hasSize(2);
		User beneficiaryUser = account.getConnections().get(0);

		int countTransfers = account.getTransfers().size();

		Optional<Rate> optRate = rateService.findById(1);
		assertThat(optRate).isNotEmpty();
		Rate rate = optRate.get();

		// WHEN
		Account modifiedAccount = accountService.makeTransfer(account, beneficiaryUser, "Pas de Transfer négatif",
				-95.90, rate);
		assertThat(modifiedAccount).isNull();

		// THEN
		optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		account = optAccount.get();

		assertThat(account.getAccountId()).isNotNull();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertEquals(account.getOpenDate().toString(), "2021-12-02");
		assertThat(account.getSolde()).isEqualTo(150.45);

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
		assertThat(account.getTransfers().get(0).getCreditedAccount().getAccountId()).isEqualTo(2);
		assertThat(account.getTransfers().get(0).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(0).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(0).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(0).getCost()).isEqualTo(0.03);

		assertEquals(account.getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-10 01:25:00"));
		assertEquals(account.getTransfers().get(1).getDescription(), "Paiement rufus");
		assertEquals(account.getTransfers().get(1).getAmount(), 37.95);
		assertThat(account.getTransfers().get(1).getCreditedAccount().getAccountId()).isEqualTo(3);
		assertThat(account.getTransfers().get(1).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(1).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(1).getRate().getValue()).isEqualTo(0.5);
		assertThat(account.getTransfers().get(1).getCost()).isEqualTo(0.19);

	}

	@Test
	@Transactional
	@Order(13)
	void makeTransferFromAccountEntity_WithoutSuccesTest_WhenSoldeLowerThanAmount() {

		// GIVEN
		Optional<Account> optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		Account account = optAccount.get();

		assertThat(account.getConnections()).isNotNull();
		assertThat(account.getConnections()).hasSize(2);
		User beneficiaryUser = account.getConnections().get(0);

		int countTransfers = account.getTransfers().size();

		Optional<Rate> optRate = rateService.findById(1);
		assertThat(optRate).isNotEmpty();
		Rate rate = optRate.get();

		// WHEN
		Account modifiedAccount = accountService.makeTransfer(account, beneficiaryUser,
				"Montant du Transfer trop élevé", 200, rate);
		assertThat(modifiedAccount).isNull();

		// THEN
		optAccount = accountService.findById(1);
		assertThat(optAccount).isNotEmpty();
		account = optAccount.get();

		assertThat(account.getAccountId()).isNotNull();
		assertThat(account.getAccountId()).isEqualTo(1);
		assertEquals(account.getOpenDate().toString(), "2021-12-02");
		assertThat(account.getSolde()).isEqualTo(150.45);

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
		assertThat(account.getTransfers().get(0).getCreditedAccount().getAccountId()).isEqualTo(2);
		assertThat(account.getTransfers().get(0).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(0).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(0).getRate().getValue()).isEqualTo(0.5);

		assertEquals(account.getTransfers().get(1).getDate(), Timestamp.valueOf("2022-01-10 01:25:00"));
		assertEquals(account.getTransfers().get(1).getDescription(), "Paiement rufus");
		assertEquals(account.getTransfers().get(1).getAmount(), 37.95);
		assertThat(account.getTransfers().get(1).getCreditedAccount().getAccountId()).isEqualTo(3);
		assertThat(account.getTransfers().get(1).getDebitedAccount().getAccountId()).isEqualTo(account.getAccountId());
		assertThat(account.getTransfers().get(1).getRate().getRateId()).isEqualTo(1);
		assertThat(account.getTransfers().get(1).getRate().getValue()).isEqualTo(0.5);

	}

	private Timestamp truncateFractionalSecondsComponent(Timestamp date) {
		date.setNanos(0);
		return date;
	}

}
