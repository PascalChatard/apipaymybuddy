package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;

@SpringBootTest
class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	final void test() {
		assertTrue(true);
	}

	@Test
	void injectedComponentIsNotNull() {
		assertThat(userRepository).isNotNull();
	}

	@Test
	void testFetchData() {
		Iterable<User> users = userRepository.findAll();
		// check if there are records
		assertNotNull(users);
		int count = 0;
		for (User p : users) {
			count++;
		}
		// there are three records in database
		assertEquals(count, 3);

		// check first record
		Optional<User> optUser1 = userRepository.findById(1);
		User user = optUser1.get();
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
		userRepository.save(user);
		// check there is Id after recording data
		assertNotNull(user.getUserId());
	}


}
