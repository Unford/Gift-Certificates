package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.AbstractDaoEntity;
import com.epam.esm.core.model.dto.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * The interface Abstract dao.
 *
 * @param <T> the type parameter
 */
public interface BaseGenericDao<T extends AbstractDaoEntity> {
    /**
     * Create t.
     *
     * @param entity the entity
     * @return the t
     */
    T create(T entity);

    /**
     * Update optional.
     *
     * @param entity the entity
     * @return the optional
     */
    Optional<T> update(T entity);


    List<T> findAll(PageRequest pageRequest);

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<T> findById(long id);

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<T> findByName(String name);

    /**
     * Delete by id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    void deleteById(long id);
}