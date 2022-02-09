package com.paymybuddy.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

}
