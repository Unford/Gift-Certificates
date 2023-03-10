package com.epam.esm.core.repository;

import com.epam.esm.core.model.domain.AbstractRepositoryEntity;
import com.epam.esm.core.model.domain.AbstractRepositoryEntity_;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBaseRepository<T extends AbstractRepositoryEntity> implements BaseGenericRepository<T> {
    private static final String NAME_COLUMN = "name";
    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> type;

    protected AbstractBaseRepository(Class<T> type) {
        this.type = type;
    }

    @Override
    @Transactional
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return entityManager.merge(entity);
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
        if (predicate != null) {
            select.where(predicate).groupBy(from.get(AbstractRepositoryEntity_.ID));
        }

        Sort sort = pageable.getSort();
        if (!sort.isEmpty()) {
            select.orderBy(extractOrders(sort, criteriaBuilder, from));
        }

        List<T> entities = entityManager.createQuery(select)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return entities;
    }

    @Override
    public Long countAllWhere(Specification<T> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> from = criteriaQuery.from(type);
        CriteriaQuery<Long> select = criteriaQuery.select(criteriaBuilder.count(from.get(AbstractRepositoryEntity_.ID)));

        Predicate predicate = specification.toPredicate(from, criteriaQuery, criteriaBuilder);
        if (predicate != null) {
            select.where(predicate);
        }
        Long count = entityManager.createQuery(select).getSingleResult();
        return count;
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
    public Optional<T> findFirstWhere(Specification<T> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        criteriaQuery.select(from).where(specification.toPredicate(from, criteriaQuery, criteriaBuilder));
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

    /**
     * It takes a Sort object and a CriteriaBuilder and returns a list of Order objects
     *
     * @param sort            The sort object that contains the sort properties and directions.
     * @param criteriaBuilder The CriteriaBuilder is used to construct criteria queries, compound selections, expressions,
     *                        predicates, orderings.
     * @param from            The root of the query.
     * @return A list of orders.
     */
    private List<Order> extractOrders(Sort sort, CriteriaBuilder criteriaBuilder, Root<T> from) {
        List<Order> orderList = new ArrayList<>();
        sort.get().forEach(order -> orderList.add(order.getDirection() == Sort.Direction.ASC ?
                criteriaBuilder.asc(from.get(order.getProperty())) :
                criteriaBuilder.desc(from.get(order.getProperty()))));
        return orderList;
    }

}
