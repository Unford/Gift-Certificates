package com.epam.esm.core.repository.specification;

import com.epam.esm.core.model.domain.AbstractRepositoryEntity;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * It's a specification that joins a table and then creates a predicate based on the criteria list
 */
public class JoinedRepositorySpecification<T extends AbstractRepositoryEntity> extends RepositorySpecification<T> {
    private final String joinTable;

    public JoinedRepositorySpecification(List<SearchCriteria> criteriaList, String joinTable) {
        super(criteriaList);
        this.joinTable = joinTable;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<Object, Object> join = root.join(joinTable);
        List<Predicate> predicates = new ArrayList<>();
        criteriaList.forEach(criteria -> {
            Predicate predicate = createPredicate(join, criteriaBuilder, criteria);
            if (predicate != null) {
                predicates.add(criteria.isNot() ? criteriaBuilder.not(predicate) : predicate);
            }
        });
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
