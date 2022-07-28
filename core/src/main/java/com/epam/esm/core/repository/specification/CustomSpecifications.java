package com.epam.esm.core.repository.specification;

import com.epam.esm.core.model.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public final class CustomSpecifications {
    private CustomSpecifications() {
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

    public static Specification<Order> whereUserId(long userId) {
        return (root, query, builder) -> builder.equal(root.get(Order_.user), userId);
    }

    public static Specification<Order> whereUserIdAndOrderId(long userId, long orderId) {
        return (root, query, builder) -> builder.and(whereUserId(userId).toPredicate(root, query, builder),
                builder.equal(root.get(Order_.ID), orderId));
    }
}
