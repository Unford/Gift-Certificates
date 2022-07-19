package com.epam.esm.core.dao.specification;

import com.epam.esm.core.model.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class JoinedDaoSpecification<T extends AbstractDaoEntity> extends DaoSpecification<T> {
    private final String joinTable;

    public JoinedDaoSpecification(List<SearchCriteria> criteriaList, String joinTable) {
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

    public static Specification<GiftCertificate> inTags(String... tags) {
        return (root, query, builder) -> {
            Join<GiftCertificate, Tag> join = root.join(GiftCertificate_.tags);
            query.groupBy(root.get(GiftCertificate_.id))
                    .having(builder.greaterThanOrEqualTo(builder.count(root.get(GiftCertificate_.id)),
                            Long.valueOf(tags.length)));
            return join.get(Tag_.name).in((Object[]) tags);
        };
    }

    public static Specification<GiftCertificate> notInTags(String... tags) {
        return (root, query, builder) -> {
            Join<GiftCertificate, Tag> join = root.join(GiftCertificate_.tags, JoinType.LEFT);
            query.groupBy(root.get(GiftCertificate_.id))
                    .having(builder.greaterThanOrEqualTo(builder.count(root.get(GiftCertificate_.id)),
                            builder.size(root.get(GiftCertificate_.tags)).as(Long.class)));
            return builder.or(builder.not(join.get(Tag_.name).in((Object[]) tags)),
                    builder.isEmpty(root.get(GiftCertificate_.tags)));
        };
    }
}
