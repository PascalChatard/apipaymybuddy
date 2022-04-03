package com.paymybuddy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.app.models.User;
import com.paymybuddy.app.repositories.UserRepository;

@SpringBootTest
class UserServiceTest {

	@Mock
	private UserRepository mockRepository;

	private AutoCloseable closeable;

	@InjectMocks
	UserService userService;

	User user;
	Iterable<User> users;

	@BeforeEach
	public void init() {

		closeable = MockitoAnnotations.openMocks(this);
		user = new User();
		user.setUserId(1);
		user.setFirstName("tartampion");
		user.setLastName("jean");
		user.setAddress("56 impasse des souris");
		user.setCity("marseille");
		user.setPhone("0632467802");
		user.setMail("durand.jean@aol.com");
		user.setPassword("jean2022");

		users = Arrays.asList(user);
	}


	@AfterEach
	public void closeService() throws Exception {
		closeable.close();
	}


	@Test
	void injectedComponentIsNotNull() {
		assertThat(mockRepository).isNotNull();
		assertThat(userService).isNotNull();
	}


	@Test
	void testFetchAllRecord() {
		// GIVEN
		when(mockRepository.findAll()).thenReturn(users);

		// WHEN
		Iterable<User> users = userService.findAll();

		// THEN
		// check if there are records
		assertThat(users).doesNotContainNull();
		assertThat(users).size().isGreaterThan(0);
		assertThat(users).size().isEqualTo(1);
		verify(mockRepository, times(1)).findAll();
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testFetchRecord() {

		// GIVEN
		when(mockRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(user));

		// WHEN
		Optional<User> optUser = userService.findById(1);

		// THEN
		assertThat(optUser).isNotEmpty();
		User userFinded = optUser.get();
		assertThat(userFinded).isEqualTo(user);
		verify(mockRepository, times(1)).findById(1);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testSaveEntity() {

		// GIVEN
		when(mockRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

		// WHEN
		User savedUser = userService.save(user);

		// THEN
		assertThat(savedUser).isEqualTo(user);
		verify(mockRepository, times(1)).save(user);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testDeleteRecordById() {

		// WHEN
		userService.deleteById(1);

		// THEN
		verify(mockRepository, times(1)).deleteById(1);
		verifyNoMoreInteractions(mockRepository);
	}


	@Test
	void testDeleteRecordByEntity() {

		// WHEN
		userService.delete(user);

		// THEN
		verify(mockRepository, times(1)).delete(user);
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testCountRecords() {

		// GIVEN
		when(mockRepository.count()).thenReturn(3L);

		// WHEN
		long nbRecords = userService.count();

		// THEN
		assertThat(nbRecords).isEqualTo(3);
		verify(mockRepository, times(1)).count();
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testExistById_True() {

		// GIVEN
		when(mockRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(true);

		// WHEN
		boolean existUser = userService.existsById(15);

		// THEN
		assertThat(existUser).isTrue();
		verify(mockRepository, times(1)).existsById(15);
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testExistById_False() {
		// GIVEN
		when(mockRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(false);

		// WHEN
		boolean existUser = userService.existsById(15);

		// THEN
		assertThat(existUser).isFalse();
		verify(mockRepository, times(1)).existsById(15);
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testCreateUserSuccessfully() {

		// GIVEN
		when(mockRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

		// WHEN
		boolean retCreateUser = userService.createUser(user);

		// THEN
		assertThat(retCreateUser).isTrue();
	}

	@Test
	void testCreateUserWithoutSuccess_WhenAtLeastOneAttributeIsEmpty() {

		// GIVEN
		user.setAddress("");
		when(mockRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

		// WHEN
		boolean retCreateUser = userService.createUser(user);

		// THEN
		assertThat(retCreateUser).isFalse();
	}

	@Test
	void testExistByEmail_True() {
		// GIVEN
		when(mockRepository.getNumberUserByEmail(ArgumentMatchers.any(String.class))).thenReturn(1);

		// WHEN
		boolean existUser = userService.existsByEmail("durand.jean@aol.com");

		// THEN
		assertThat(existUser).isTrue();
		verify(mockRepository, times(1)).getNumberUserByEmail("durand.jean@aol.com");
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testExistByEmail_False() {
		// GIVEN
		when(mockRepository.getNumberUserByEmail(ArgumentMatchers.any(String.class))).thenReturn(0);

		// WHEN
		boolean existUser = userService.existsByEmail("durand.jean@aol.fr");

		// THEN
		assertThat(existUser).isFalse();
		verify(mockRepository, times(1)).getNumberUserByEmail("durand.jean@aol.fr");
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	void testCreateUserWithoutSuccess_WhenEmaiAttributeAlreadyExist() {

		// GIVEN
		user.setMail("alejeune@outlook.com");
		when(mockRepository.getNumberUserByEmail(ArgumentMatchers.any(String.class))).thenReturn(1);
		when(mockRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

		// WHEN
		boolean retCreateUser = userService.createUser(user);

		// THEN
		assertThat(retCreateUser).isFalse();
	}


}
