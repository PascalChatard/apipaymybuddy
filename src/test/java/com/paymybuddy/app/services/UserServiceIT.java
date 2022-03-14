package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
		assertThat(users).size().isEqualTo(3);
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
		assertThat(nbRecords).isEqualTo(3);
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

}
