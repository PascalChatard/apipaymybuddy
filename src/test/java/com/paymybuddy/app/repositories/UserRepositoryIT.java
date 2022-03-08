package com.paymybuddy.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import com.paymybuddy.app.models.User;

@Sql("data.sql")
//@DataJpaTest
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryIT {

	@Autowired
	private UserRepository userRepository;


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {

		// THEN
		assertThat(userRepository).isNotNull();
	}

	@Test
	@Order(2)
	void testFetchAllData() {

		// WHEN
		Iterable<User> users = userRepository.findAll();

		// THEN
		// check if there are records
		assertThat(users).doesNotContainNull();
		assertThat(users).size().isGreaterThan(0);
		assertThat(users).size().isEqualTo(3);
	}

	@Test
	@Order(3)
	void testFetchRecord() {

		// WHEN
		// check that exist record with ID's 1
		Optional<User> optUser = userRepository.findById(1);
		assertThat(optUser).isNotEmpty();

		// THEN
		// check attributes values
		User user = optUser.get();
		assertThat(user.getUserId()).isEqualTo(1);
		assertThat(user.getFirstName()).isEqualTo("durand");
		assertThat(user.getLastName()).isEqualTo("jean");
		assertThat(user.getAddress()).isEqualTo("56 impasse des souris");
		assertThat(user.getCity()).isEqualTo("marseille");
		assertThat(user.getPhone()).isEqualTo("0632467802");
		assertThat(user.getMail()).isEqualTo("durand.jean@aol.com");
	}

	@Test
	@Order(4)
	void testRecordData() {

		// GIVEN
		User user = new User();
		user.setFirstName("leroi");
		user.setLastName("merlin");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("leroi.merlin@orange.fr");

		// WHEN
		User savedUser = userRepository.save(user);

		// THEN
		assertThat(savedUser).isEqualTo(user);
	}

	@Test
	@Order(5)
	void testRecordData_ThrowException_WhenMAilAttributAlreadyExist() {

		// GIVEN
		User user = new User();
		user.setFirstName("alain");
		user.setLastName("Lejeune");
		user.setAddress("7 rue bricolos");
		user.setCity("nice");
		user.setPhone("0493556231");
		user.setMail("alejeune@outlook.com");

		// THEN
		// check there is no Id before recording data
		assertThat(user.getUserId()).isNull();
		// check that a save user with a already exist mail's attribute throws an
		// exception
		assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> userRepository.save(user));
	}

//	@Test
//	@Order(6)
////	@Rollback(false)
////	@Modifying
////	@Transactional
//	void testDeleteRecordById() {
//		userRepository.deleteById(1);
//
//		// THEN
//		// assertDoesNotThrow(() -> userRepository.deleteById(1));
//		// assertThat(userRepository.existsById(1)).isFalse();
//		assertThat(userRepository.findById(1)).isNull();
//	}

	@Test
	@Order(7)
	void testDeleteRecordById_Throw_EmptyResultDataAccessException() {

		// THEN
		// check that delete a record with a ID out of bound throws an
		// exception
		assertThatExceptionOfType(EmptyResultDataAccessException.class)
				.isThrownBy(() -> userRepository.deleteById(1000));
	}

//	@Test
//	@Order(8)
//	void testDeleteRecord() {
//
//		// GIVEN
//		// check first record
//		Optional<User> optUser = userRepository.findById(1);
//		User user = optUser.get();
//		assertThat(user).isNotNull();
//
//		// WHEN
//		userRepository.delete(user);
//
//		// THEN
//		// check that get optional with finding record with ID 1 throws an exception
//		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> userRepository.findById(1).get());
//		// check that finding record with ID 1 return empty optional
//		assertThat(userRepository.findById(1)).isEmpty();
//	}

	@Test
	@Order(9)
	void testCountData() {

		// THEN
		// there are three records in database
		assertThat(userRepository.count()).isEqualTo(3);
	}

	@Test
	@Order(10)
	void testExistById_True() {

		// THEN
		// there are three records in database and ID's are (1,2,3)
		assertThat(userRepository.existsById(1)).isTrue();
	}

	@Test
	@Order(11)
	void testExistById_False() {

		// THEN
		// there are three records in database and ID's are (1,2,3)
		assertThat(userRepository.existsById(100)).isFalse();
	}

}
