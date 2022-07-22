package com.epam.esm.core.repository;

import com.epam.esm.core.model.domain.AbstractRepositoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * The interface Abstract dao.
 *
 * @param <T> the type parameter
 */
public interface BaseGenericRepository<T extends AbstractRepositoryEntity> {
    /**
     * Create t.
     *
     * @param entity the entity
     * @return the t
     */
    T create(T entity);


    T update(T entity);


    List<T> findAll(Pageable pageable);
    List<T> findAll(Specification<T> specification, Pageable pageable);
    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<T> findById(long id);

    Optional<T> findFirstBy(Specification<T> specification);

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
