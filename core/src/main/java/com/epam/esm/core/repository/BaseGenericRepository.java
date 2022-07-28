package com.epam.esm.core.repository;

import com.epam.esm.core.model.domain.AbstractRepositoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface BaseGenericRepository<T extends AbstractRepositoryEntity> {
    T create(T entity);


    T update(T entity);


    List<T> findAll(Pageable pageable);
    List<T> findAll(Specification<T> specification, Pageable pageable);

    Long countAllWhere(Specification<T> specification);

    Optional<T> findById(long id);

    Optional<T> findFirstWhere(Specification<T> specification);

    Optional<T> findByName(String name);

    void deleteById(long id);
}
