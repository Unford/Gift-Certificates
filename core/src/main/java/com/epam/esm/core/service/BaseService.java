package com.epam.esm.core.service;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.request.SimplePageRequest;

import java.util.List;

/**
 * A base interface for all services.
 */
public interface BaseService<T> {
    /**
     * Create a new entity.
     *
     * @param entity The entity to create.
     * @return The entity that was created.
     */
    T create(T entity) throws ServiceException;

    /**
     * Find all the T's in the database, and return them in a list, starting at the given page number,
     * and with the given page size.
     *
     * @param simplePage This is a class that contains the page number and the page size.
     * @return A list of objects of type T.
     */
    List<T> findAll(SimplePageRequest simplePage);

    /**
     * Finds an entity by id and throws a ServiceException if it can't be found.
     *
     * @param id The id of the entity to find.
     * @return The object of type T that has the id of the parameter.
     */
    T findById(long id) throws ServiceException;

    /**
     * Update the entity in the database.
     *
     * @param entity The entity to be updated.
     * @return The updated entity.
     */
    T update(T entity) throws ServiceException;

    /**
     * Deletes the entity with the given id.
     *
     * @param id The id of the entity to be deleted.
     */
    void deleteById(long id) throws ServiceException;
}
