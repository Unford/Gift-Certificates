package com.epam.esm.core.dao.specification;

import com.epam.esm.core.model.domain.AbstractDaoEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AbstractDaoSpecification<T extends AbstractDaoEntity> implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and();
    }
}
