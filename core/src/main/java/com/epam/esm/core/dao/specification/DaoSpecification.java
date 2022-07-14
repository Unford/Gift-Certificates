package com.epam.esm.core.dao.specification;

import com.epam.esm.core.model.domain.AbstractDaoEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

public class DaoSpecification<T extends AbstractDaoEntity> implements Specification<T> {
    private static final String PERCENT_SIGN = "%";
    private final List<SearchCriteria> criteriaList;

    public DaoSpecification(List<SearchCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        criteriaList.forEach(criteria -> {
            From from = findFrom(criteria, root);
            Predicate predicate = null;
            switch (criteria.getOperation()) {
                case EQUAL:
                    predicate = criteriaBuilder.equal(from.get(criteria.getKey()), criteria.getValue());
                    break;
                case LIKE:
                    predicate = criteriaBuilder.like(from.get(criteria.getKey()),
                            PERCENT_SIGN + criteria.getValue() + PERCENT_SIGN);
                    break;
                case LESS_THAN:
                    predicate = criteriaBuilder.lessThan(from.get(criteria.getKey()), criteria.getValue());
                    break;
                case LESS_THAN_EQUAL:
                    predicate = criteriaBuilder.lessThanOrEqualTo(from.get(criteria.getKey()), criteria.getValue());
                    break;
                case GREATER_THAN:
                    predicate = criteriaBuilder.greaterThan(from.get(criteria.getKey()), criteria.getValue());
                    break;
                case GREATER_THAN_EQUAL:
                    predicate = criteriaBuilder.greaterThanOrEqualTo(from.get(criteria.getKey()), criteria.getValue());
                    break;
            }
            if (predicate != null) {
                predicates.add(criteria.isNot() ? criteriaBuilder.not(predicate) : predicate);
            }
        });
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private From findFrom(SearchCriteria searchCriteria, Root<T> root) {
        List<String> joinTables = searchCriteria.getJoinTables();
        Join join = null;
        for (String table : joinTables) {
            if (join == null) {
                join = root.join(table);
            } else {
                join = join.join(table);
            }
        }
        return join == null ? root : join;
    }
}
