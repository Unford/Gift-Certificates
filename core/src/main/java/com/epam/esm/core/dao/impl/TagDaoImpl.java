package com.epam.esm.core.dao.impl;

import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.domain.Tag_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * The type Tag dao.
 */
@Repository
public class TagDaoImpl implements TagDao {//todo jpa

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Tag create(Tag entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag dao");
    }

    @Override
    public Page<Tag> findAll(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> from = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> select = criteriaQuery.select(from);
        TypedQuery<Tag> typedQuery = entityManager.createQuery(select)
                .setFirstResult((pageNumber - 1) * pageSize)//todo -1
                .setMaxResults(pageSize);
        List<Tag> tags = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Tag.class)));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(tags, PageRequest.of(pageNumber, pageSize), total);


    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> from = criteriaQuery.from(Tag.class);
        criteriaQuery.select(from).where(criteriaBuilder.equal(from.get(Tag_.name), name));
        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
         entityManager.remove(findById(id).get());
        return true;//todo
    }

    @Override
    public boolean isAnyLinksToTag(long id) {
        return !findById(id).get().getGiftCertificates().isEmpty();
    }
}
