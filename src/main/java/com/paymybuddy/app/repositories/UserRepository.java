package com.paymybuddy.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
