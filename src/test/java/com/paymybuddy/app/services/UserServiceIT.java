package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.User;

@SpringBootTest
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceIT {

	@Autowired
	UserService userService;


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(userService).isNotNull();
	}

	@Test
	@Order(2)
	void testFetchRecord() {

		// WHEN
		Optional<User> optUser = userService.findById(1);

		// THEN
		assertThat(optUser).isNotEmpty();
		User user = optUser.get();
		assertThat(user).isNotNull();
		assertThat(user.getUserId()).isNotNull();
		assertThat(user.getUserId()).isEqualTo(1);
		assertEquals(user.getFirstName(), "durand");
		assertEquals(user.getLastName(), "jean");
		assertEquals(user.getAddress(), "56 impasse des souris");
		assertEquals(user.getCity(), "marseille");
		assertEquals(user.getPhone(), "0632467802");
		assertEquals(user.getMail(), "durand.jean@aol.com");
	}


	@Test
	@Order(3)
	void testFetchAllRecord() {

		// WHEN
		Iterable<User> users = userService.findAll();

		// THEN
		// check if there are records
		assertThat(users).doesNotContainNull();
		assertThat(users).size().isGreaterThan(0);
		assertThat(users).size().isEqualTo(4);
	}


	@Test
	@Order(4)
	void testSaveEntity() {

		// GIVEN
		User user = new User();
		user.setFirstName("leroi");
		user.setLastName("merlin");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("leroi.merlin@orange.fr");

		// WHEN
		User savedUser = userService.save(user);

		// THEN
		assertThat(savedUser).isNotNull();
		assertThat(savedUser).isEqualTo(user);
	}


	@Test
	@Order(5)
	void testDeleteRecordById() {

		// GIVEN
		Optional<User> optUser = userService.findById(1);
		assertThat(optUser).isNotEmpty();
		User user = optUser.get();

		// WHEN
		userService.deleteById(user.getUserId());

		// THEN
		assertThat(userService.existsById(1)).isFalse();
	}

	@Test
	@Order(6)
	void testDeleteRecordByEntity() {

		// GIVEN
		Optional<User> optUser = userService.findById(1);
		assertThat(optUser).isNotEmpty();
		User user = optUser.get();

		// WHEN
		userService.delete(user);

		// THEN
		assertThat(userService.existsById(user.getUserId())).isFalse();
	}


	@Test
	@Order(7)
	void testCountRecords() {

		// WHEN
		long nbRecords = userService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(4);
	}

	@Test
	@Order(8)
	void testExistById_True() {

		// WHEN
		boolean existUser = userService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	@Order(9)
	void testExistById_False() {

		// WHEN
		boolean existUser = userService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}

	@Test
	@Order(10)
	void testSaveEntityThrowDataIntegrityViolationException() {

		// GIVEN
		User user = new User();
		user.setFirstName("leroi");
		user.setLastName("merlin");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("alejeune@outlook.com");

		// THEN
		// check that save a user with a mail already exist in DB throws an
		// exception
		assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> userService.save(user));
	}

	@Test
	@Order(11)
	void accessAccountAndTransfersDataFromUserEntityTest() {

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

		assertThat(user.getAccountUser().getConnections()).isNotNull();
		assertThat(user.getAccountUser().getConnections()).hasSize(1);
		assertEquals(user.getAccountUser().getConnections().get(0).getLastName(), "alain");
		assertEquals(user.getAccountUser().getConnections().get(0).getFirstName(), "Lejeune");
		assertEquals(user.getAccountUser().getConnections().get(0).getAddress(), "32 avenue léon bloum");
		assertEquals(user.getAccountUser().getConnections().get(0).getCity(), "pertuis");
		assertEquals(user.getAccountUser().getConnections().get(0).getPhone(), "0490255633");
		assertEquals(user.getAccountUser().getConnections().get(0).getMail(), "alejeune@outlook.com");
		assertEquals(user.getAccountUser().getConnections().get(0).getPassword(), "alain2022");

	}

	@Test
	@Transactional
	@Order(12)
	void createUserAndAccountTest() {

		// GIVEN

		// new user
		User user = new User();
		user.setFirstName("annie");
		user.setLastName("Legrand");
		user.setAddress("9 rue Donjon");
		user.setCity("perpignan");
		user.setPhone("0593556231");
		user.setMail("annie.legrand@outlook.com");
		user.setPassword("annie2022");

		Date date = new Date(System.currentTimeMillis());

		// WHEN
		boolean retCreateUser = userService.createUser(user);

		// THEN
		assertThat(retCreateUser).isTrue();

		Iterable<User> users = userService.findByLastname("Legrand");
		// check if there are records
		assertThat(users).isNotNull();
		assertThat(users).doesNotContainNull();
		assertThat(users).size().isGreaterThan(0);
		assertThat(users).size().isEqualTo(1);

		// user informations
		assertEquals(users.iterator().next().getFirstName(), user.getFirstName().toUpperCase());
		assertEquals(users.iterator().next().getLastName(), user.getLastName().toUpperCase());
		assertEquals(users.iterator().next().getAddress(), user.getAddress().toUpperCase());
		assertEquals(users.iterator().next().getCity(), user.getCity().toUpperCase());
		assertEquals(users.iterator().next().getPhone(), user.getPhone());
		assertEquals(users.iterator().next().getMail(), user.getMail());
		assertEquals(users.iterator().next().getPassword(), user.getPassword());
		// account informations
		assertThat(users.iterator().next().getAccountUser()).isNotNull();
		assertThat(users.iterator().next().getAccountUser().getOpenDate()).isEqualTo(Date.valueOf(date.toString()));
		assertThat(users.iterator().next().getAccountUser().getSolde()).isEqualTo(0);

		// connections informations
		assertThat(users.iterator().next().getAccountUser().getConnections()).isNotNull();
		assertThat(user.getAccountUser().getConnections()).hasSize(0);

		// transfer information
		assertThat(user.getAccountUser().getTransfers()).isNotNull();
		assertThat(user.getAccountUser().getTransfers().size()).isEqualTo(0);

	}

}
