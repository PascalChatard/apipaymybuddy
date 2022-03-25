package com.paymybuddy.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public class GenericService<E> {

	@Autowired
	CrudRepository<E, Integer> repository;

	public Optional<E> findById(Integer id) {
		return repository.findById(id);
	}

	public Iterable<E> findAll() {
		return repository.findAll();
	}

	public E save(E entity) {
		return repository.save(entity);
	}

	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	public void delete(E entity) {
		repository.delete(entity);
	}

	public boolean existsById(Integer id) {
		return repository.existsById(id);
	}

	public long count() {
		return repository.count();
	}

}
