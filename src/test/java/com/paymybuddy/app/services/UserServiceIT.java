package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	final void test() {
		assertTrue(true);
	}

	@Test
	void injectedComponentIsNotNull() {
		assertThat(userService).isNotNull();
	}

	@Test
	void testFetchRecord() {
		// get first record
		Optional<User> optUser1 = userService.findById(1);
		User user = optUser1.get();
		// check first record
		assertThat(user).isNotNull();
		assertNotNull(user.getUserId());
		assertEquals(user.getFirstName(), "durand");
		assertEquals(user.getLastName(), "jean");
		assertEquals(user.getAddress(), "56 impasse des souris");
		assertEquals(user.getCity(), "marseille");
		assertEquals(user.getPhone(), "0632467802");
		assertEquals(user.getMail(), "durand.jean@aol.com");
	}

	@Test
	void testFetchData() {
		Iterable<User> users = userService.findAll();
		// check if there are records
		assertNotNull(users);
		int count = 0;
		for (User p : users) {
			count++;
		}
		// there are three records in database
		assertEquals(count, 3);
	}

	@Test
	void testRecordData() {
		User user = new User();
		user.setFirstName("leroi");
		user.setLastName("merlin");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("leroi.merlin@orange.fr");

		// check there is no Id before recording data
		assertNull(user.getUserId());
		userService.save(user);
		// check there is Id after recording data
		assertNotNull(user.getUserId());
	}

	@Test
	void testDeleteRecordById() {
		// get first record
		Optional<User> optUser1 = userService.findById(1);
		User user = optUser1.get();

		// check there is Id after recording data
		assertNotNull(user.getUserId());

		userService.deleteById(user.getUserId());

		// check there is no Id before recording data
		// assertNull(user.getUserId());
	}

	@Test
	void testDeleteRecordByEntity() {
		// check first record
		User user = new User();
		user.setFirstName("leroi");
		user.setLastName("merlin");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("leroi.merlin@orange.fr");

		// check there is Id after recording data
		// assertNotNull(user.getUserId());

		userService.delete(user);

		// check there is no Id before recording data
		assertNull(user.getUserId());
	}

}
