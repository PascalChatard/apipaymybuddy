package com.paymybuddy.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.models.Rate;

@Repository
public interface RateRepository extends CrudRepository<Rate, Integer> {

}
