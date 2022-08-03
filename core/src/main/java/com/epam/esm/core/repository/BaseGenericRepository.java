package com.epam.esm.core.repository;

import com.epam.esm.core.model.domain.AbstractRepositoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Base interface for all repositories.
 */
public interface BaseGenericRepository<T extends AbstractRepositoryEntity> {
    /**
     * Create a new entity of type T.
     *
     * @param entity The entity to be created.
     * @return The entity that was created.
     */
    T create(T entity);

    /**
     * Update the entity in the database.
     *
     * @param entity The entity to be updated.
     * @return The updated entity.
     */
    T update(T entity);


    /**
     * Find all entities of type T, and return them in a pageable format.
     *
     * @param pageable This is the Pageable object that contains the page number, page size, and sort order.
     * @return A Page<T> object.
     */
    List<T> findAll(Pageable pageable);

    /**
     * Find all entities that match the given specification, and return them in a pageable fashion.
     *
     * @param specification A specification is a predicate over an entity. It is used to restrict the number of results
     *                      returned by a query.
     * @param pageable      This is the Pageable object that contains the page number, page size, and sort order.
     * @return A Page of entities meeting the paging restriction provided in the Pageable object and the specification
     * restriction provided in the Specification object.
     */
    List<T> findAll(Specification<T> specification, Pageable pageable);

    /**
     * Count all entities that match the given specification.
     *
     * @param specification The specification to use to filter the results.
     * @return A Long object.
     */
    Long countAllWhere(Specification<T> specification);

    /**
     * Finds an entity by its id.
     *
     * @param id The id of the entity to be retrieved.
     * @return Optional<T>
     */
    Optional<T> findById(long id);

    /**
     * Find the first element that matches the given specification.
     *
     * @param specification A specification that defines the query to be executed.
     * @return Optional<T>
     */
    Optional<T> findFirstWhere(Specification<T> specification);

    /**
     * Find an entity by name, and return it if found, otherwise return null.
     *
     * @param name The name of the entity.
     * @return Optional<T>
     */
    Optional<T> findByName(String name);

    /**
     * Deletes the entity with the given id.
     *
     * @param id The id of the object to delete.
     */
    void deleteById(long id);
}
