package com.paymybuddy.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Transfer;
import com.paymybuddy.app.repositories.TransferRepository;

@Service
public class TransferService {

	@Autowired
	TransferRepository transferRepository;

	public Optional<Transfer> findById(Integer userId) {
		return transferRepository.findById(userId);
	}

	public Iterable<Transfer> findAll() {
		return transferRepository.findAll();
	}

	public Transfer save(Transfer user) {
		return transferRepository.save(user);
	}

	public void deleteById(Integer userId) {
		transferRepository.deleteById(userId);
	}

	public void delete(Transfer user) {
		transferRepository.delete(user);
	}

	public boolean existsById(Integer userId) {
		return transferRepository.existsById(userId);
	}

	public long count() {
		return transferRepository.count();
	}

}
