package com.apipaymybuddy.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apipaymybuddy.app.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {


	/**
	 * findByLastname - Get users with last name matching given name
	 * 
	 * @param name The lastname of the user to find
	 * @return A Iterable list of users found
	 */
	public Iterable<User> findByLastName(String name);

	/**
	 * getNumberUserByEmail - Get number of user with given e-mail
	 * 
	 * @param mail The e-mail of the user to find
	 * @return number of user with this e-mail
	 */
	@Query(value = "SELECT COUNT(*) FROM user WHERE mail = ?1", nativeQuery = true)
	public int getNumberUserByEmail(@Param("mail") String mail);

	/**
	 * findByMail - Get users with mail matching given email
	 * 
	 * @param email The email of the user to find
	 * @return A Iterable list of users found
	 */
	public Optional<User> findByMail(String email);

}
