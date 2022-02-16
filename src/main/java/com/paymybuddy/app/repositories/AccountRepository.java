package com.paymybuddy.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.models.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

}
