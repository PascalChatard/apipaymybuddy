package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.User;

@SpringBootTest
@Sql("user.sql")
class UserServiceIT {

	@Autowired
	UserService userService;


	@Test
	void injectedComponentIsNotNull() {
		assertThat(userService).isNotNull();
	}

	@Test
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
	void testCountRecords() {

		// WHEN
		long nbRecords = userService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3);
	}

	@Test
	void testExistById_True() {

		// WHEN
		boolean existUser = userService.existsById(2);

		// THEN
		assertThat(existUser).isTrue();
	}

	@Test
	void testExistById_False() {

		// WHEN
		boolean existUser = userService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
	}

}
