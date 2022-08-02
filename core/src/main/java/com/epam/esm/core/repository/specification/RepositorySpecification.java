package com.epam.esm.core.repository.specification;

import com.epam.esm.core.model.domain.AbstractRepositoryEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * It takes a list of SearchCriteria objects and creates a JPA Predicate object that can be used to query the database
 */
public class RepositorySpecification<T extends AbstractRepositoryEntity> implements Specification<T> {
    private static final String PERCENT_SIGN = "%";
    protected final List<SearchCriteria> criteriaList;

    public RepositorySpecification(List<SearchCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        criteriaList.forEach(criteria -> {
            if (criteria.getValue() != null && !criteria.getValue().toString().isEmpty()) {
                Predicate predicate = createPredicate(root, criteriaBuilder, criteria);
                if (predicate != null) {
                    predicates.add(criteria.isNot() ? criteriaBuilder.not(predicate) : predicate);
                }
            }
        });
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * It creates a predicate based on the search criteria
     *
     * @param root The root of the query.
     * @param criteriaBuilder This is the criteria builder that is used to create the predicate.
     * @param criteria The criteria object that contains the key, value and operation.
     * @return A Predicate object.
     */
    protected Predicate createPredicate(From<?, ?> root, CriteriaBuilder criteriaBuilder, SearchCriteria criteria) {
        Predicate predicate = null;
        String value = criteria.getValue().toString();
        switch (criteria.getOperation()) {
            case EQUAL:
                predicate = criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
                break;
            case LIKE:
                predicate = criteriaBuilder.like(root.get(criteria.getKey()),
                        PERCENT_SIGN + value + PERCENT_SIGN);
                break;
            case LESS_THAN:
                predicate = criteriaBuilder.lessThan(root.get(criteria.getKey()), value);
                break;
            case LESS_THAN_EQUAL:
                predicate = criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), value);
                break;
            case GREATER_THAN:
                predicate = criteriaBuilder.greaterThan(root.get(criteria.getKey()), value);
                break;
            case GREATER_THAN_EQUAL:
                predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), value);
                break;
            case IN:
                predicate = root.get(criteria.getKey()).in((Object[]) criteria.getValue());
                break;
        }
        return predicate;
    }


}
