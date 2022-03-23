package com.paymybuddy.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Rate;
import com.paymybuddy.app.repositories.RateRepository;

@Service
public class RateService {

	@Autowired
	RateRepository rateRepository;

	public Optional<Rate> findById(Integer rateId) {
		return rateRepository.findById(rateId);
	}

	public Iterable<Rate> findAll() {
		return rateRepository.findAll();
	}

	public Rate save(Rate rate) {
		return rateRepository.save(rate);
	}

	public void deleteById(Integer rateId) {
		rateRepository.deleteById(rateId);
	}

	public void delete(Rate rate) {
		rateRepository.delete(rate);
	}

	public boolean existsById(Integer rateId) {
		return rateRepository.existsById(rateId);
	}

	public long count() {
		return rateRepository.count();
	}

}
