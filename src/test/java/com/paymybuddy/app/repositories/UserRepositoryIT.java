package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.User;

@Sql("user.sql")
@SpringBootTest
class UserRepositoryIT {

	@Autowired
	private UserRepository userRepository;


	@Test
	void injectedComponentIsNotNull() {
		assertThat(userRepository).isNotNull();
	}

	@Test
	void testFetchAllData() {
		Iterable<User> users = userRepository.findAll();
		// check if there are records
		assertThat(users).isNotNull();
		assertThat(users).size().isGreaterThan(0);
		assertThat(users).size().isEqualTo(3);
	}

	@Test
	void testFetchRecord() {
		// check first record
		Optional<User> optUser1 = userRepository.findById(1);
		User user = optUser1.get();
		assertThat(user).isNotNull();
		assertThat(user.getUserId()).isNotNull();
		assertThat(user.getFirstName()).isEqualTo("durand");
		assertThat(user.getLastName()).isEqualTo("jean");
		assertThat(user.getAddress()).isEqualTo("56 impasse des souris");
		assertThat(user.getCity()).isEqualTo("marseille");
		assertThat(user.getPhone()).isEqualTo("0632467802");
		assertThat(user.getMail()).isEqualTo("durand.jean@aol.com");
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
		assertThat(user.getUserId()).isNull();
		User savedUser = userRepository.save(user);
		// check there is Id after recording data
		assertThat(savedUser.getUserId()).isNotNull();
	}

	@Test
	void testRecordData_ThrowException_WhenMAilAttributAlreadyExist() {
		User user = new User();
		user.setFirstName("alain");
		user.setLastName("Lejeune");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("alejeune@outlook.com");

		// check there is no Id before recording data
		assertThat(user.getUserId()).isNull();
		assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> userRepository.save(user));

	}

	@Test
	void testDeleteRecordById() {

		assertDoesNotThrow(() -> userRepository.deleteById(1));
		// assertThatCode(() ->
		// userRepository.deleteById(1)).doesNotThrowAnyException();
		// assertThatNoException().isThrownBy(userRepository.deleteById(1));
	}

	@Test
	void testDeleteRecordById_ThrowEmptyResultDataAccessException() {

		assertThatExceptionOfType(EmptyResultDataAccessException.class)
				.isThrownBy(() -> userRepository.deleteById(1000));
	}

	@Test
	void testDeleteRecord() {

		// check first record
		Optional<User> optUser1 = userRepository.findById(1);
		User user = optUser1.get();
		assertThat(user).isNotNull();
		userRepository.delete(user);
		// check that get optional with finding record with ID 1 throws an exception
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> userRepository.findById(1).get());
		// check that finding record with ID 1 return empty optional
		assertThat(userRepository.findById(1)).isEmpty();
	}

	@Test
	void testCountData() {
		// there are three records in database
		assertThat(userRepository.count()).isEqualTo(3);
	}

	@Test
	void testExistById_True() {
		// there are three records in database and ID's are (1,2,3)
		assertThat(userRepository.existsById(1)).isTrue();
	}

	@Test
	void testExistById_False() {
		// there are three records in database and ID's are (1,2,3)
		assertThat(userRepository.existsById(100)).isFalse();
	}

}
