package com.paymybuddy.app.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {


	public Iterable<User> findByLastName(String name);

	@Query(value = "SELECT COUNT(*) FROM user WHERE mail = ?1", nativeQuery = true)
	public int existsByEmail(@Param("mail") String mail);

}
