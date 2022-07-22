package com.epam.esm.core.service;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.request.PageRequestParameters;

import java.util.List;

/**
 * The interface Basic service.
 *
 * @param <T> the type parameter
 */
public interface BaseService<T> {
    /**
     * Create t.
     *
     * @param entity the entity
     * @return the t
     * @throws ServiceException the service exception
     */
    T create(T entity) throws ServiceException;

    /**
     * Find all list.
     *
     * @return the list
     */
    List<T> findAll(PageRequestParameters pageRequestParameters);

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     * @throws ServiceException the service exception
     */
    T findById(long id) throws ServiceException;

    /**
     * Update t.
     *
     * @param entity the entity
     * @return the t
     * @throws ServiceException the service exception
     */
    T update(T entity) throws ServiceException;

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    void deleteById(long id) throws ServiceException;
}
