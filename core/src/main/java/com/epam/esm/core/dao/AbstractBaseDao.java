package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.AbstractDaoEntity;
import com.epam.esm.core.model.dto.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBaseDao<T extends AbstractDaoEntity> implements BaseGenericDao<T>{
    private static final String NAME_COLUMN = "name";
    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> type;

    protected AbstractBaseDao(Class<T> type){
        this.type = type;
    }

    @Override
    @Transactional
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<T> update(T entity) {
        return Optional.ofNullable(entityManager.merge(entity));
    }

    @Override
    public List<T> findAll(PageRequest pageRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        CriteriaQuery<T> select = criteriaQuery.select(from);
        List<T> entities = entityManager.createQuery(select)
                .setFirstResult((pageRequest.getPage() - 1) * pageRequest.getSize())
                .setMaxResults(pageRequest.getSize())
                .getResultList();
        return entities;
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(entityManager.find(type, id));
    }

    @Override
    public Optional<T> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        criteriaQuery.select(from).where(criteriaBuilder.equal(from.get(NAME_COLUMN), name));
        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        entityManager.remove(findById(id).get());

    }
}
