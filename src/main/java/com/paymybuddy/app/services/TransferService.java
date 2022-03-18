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

	public Optional<Transfer> findById(Integer transferId) {
		return transferRepository.findById(transferId);
	}

	public Iterable<Transfer> findAll() {
		return transferRepository.findAll();
	}

	public Transfer save(Transfer transfer) {
		return transferRepository.save(transfer);
	}

	public void deleteById(Integer transferId) {
		transferRepository.deleteById(transferId);
	}

	public void delete(Transfer transfer) {
		transferRepository.delete(transfer);
	}

	public boolean existsById(Integer transferId) {
		return transferRepository.existsById(transferId);
	}

	public long count() {
		return transferRepository.count();
	}

}
