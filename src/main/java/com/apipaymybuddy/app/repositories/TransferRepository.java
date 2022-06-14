package com.apipaymybuddy.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.apipaymybuddy.app.models.Transfer;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Integer> {

}
