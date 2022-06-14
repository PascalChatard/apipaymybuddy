package com.apipaymybuddy.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.apipaymybuddy.app.models.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

}
