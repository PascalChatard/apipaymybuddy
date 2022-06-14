package com.apipaymybuddy.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericService<E> {

	/**
	 * Injects entity repository bean
	 */
	@Autowired
	CrudRepository<E, Integer> repository;

	/**
	 * findById - Get one entity
	 * 
	 * @param id The id of the entity
	 * @return An Entity object full filled
	 */
	public Optional<E> findById(Integer id) {
		log.debug("Debut methode findById, arg: entityId ({})", id);

		Optional<E> optEntity = repository.findById(id);

		log.debug("Fin methode findById");
		return optEntity;
	}

	/**
	 * findAll - Get all entities
	 * 
	 * @return - An Iterable object of Entity full filled
	 */
	public Iterable<E> findAll() {
		log.debug("Debut methode findAll, sans arg.");

		Iterable<E> iterableEntity = repository.findAll();

		log.debug("Fin methode findAll");
		return iterableEntity;
	}

	/**
	 * save - Add a new entity, or update a existing entity
	 * 
	 * @param entity An object Entity
	 * @return The entity object saved/updated
	 */
	public E save(E entity) {
		log.debug("Debut methode save, arg: {} ({})", entity.getClass(), entity);

		E entitySaved = repository.save(entity);

		log.debug("Fin methode save");
		return entitySaved;
	}

	/**
	 * deleteById - Delete an entity
	 * 
	 * @param id - The id of the entity to delete
	 */
	public void deleteById(Integer id) {
		log.debug("Debut methode deleteById, arg: entityId ({})", id);

		repository.deleteById(id);

		log.debug("Fin methode deleteById");
	}

	/**
	 * delete - Delete an entity
	 * 
	 * @param entity - The entity to delete
	 */
	public void delete(E entity) {
		log.debug("Debut methode delete, arg: {} ({})", entity.getClass(), entity);

		repository.delete(entity);

		log.debug("Fin methode delete");
	}

	/**
	 * existsById - Returns whether an entity with the given id exists.
	 * 
	 * @param id - The id of the entity to delete
	 * @return true if an entity with the given id exists, false otherwise
	 */
	public boolean existsById(Integer id) {
		log.debug("Debut methode existsById, arg: entityId ({})", id);

		boolean status = repository.existsById(id);

		log.debug("Fin methode existsById");
		return status;
	}

	/**
	 * count - Returns the number of entities available
	 * 
	 * @return the number of entities
	 */
	public long count() {
		log.debug("Debut methode count, sans arg.");

		long numberOfEntities = repository.count();

		log.debug("Fin methode count");
		return numberOfEntities;
	}

}
