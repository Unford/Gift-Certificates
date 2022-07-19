package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.AbstractDaoEntity;
import com.epam.esm.core.model.domain.AbstractDaoEntity_;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBaseDao<T extends AbstractDaoEntity> implements BaseGenericDao<T> {
    private static final String NAME_COLUMN = "name";
    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> type;

    protected AbstractBaseDao(Class<T> type) {
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
    public List<T> findAll(Pageable pageable) {
        return this.findAll(Specification.where(null), pageable);
    }

    @Override
    public List<T> findAll(Specification<T> specification, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        CriteriaQuery<T> select = criteriaQuery.select(from);

        Predicate predicate = specification.toPredicate(from, criteriaQuery, criteriaBuilder);
        if (predicate != null && !predicate.getExpressions().isEmpty()) {
            select.where(predicate).groupBy(from.get(AbstractDaoEntity_.ID));
        }

        Sort sort = pageable.getSort();
        if (!sort.isEmpty()) {
            List<Order> orderList = new ArrayList<>();
            sort.get().forEach(order -> orderList.add(order.getDirection() == Sort.Direction.ASC ?
                    criteriaBuilder.asc(from.get(order.getProperty())) :
                    criteriaBuilder.desc(from.get(order.getProperty()))));
            select.orderBy(orderList);
        }
        List<T> entities = entityManager.createQuery(select)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
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
