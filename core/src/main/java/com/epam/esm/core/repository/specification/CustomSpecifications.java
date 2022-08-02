package com.epam.esm.core.repository.specification;

import com.epam.esm.core.model.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

/**
 * It contains static methods that return specifications for filtering entities
 */
public final class CustomSpecifications {
    private CustomSpecifications() {
    }

    /**
     * "Select all gift certificates that are in the specified tags."
     *
     * @param tags tag names
     * @return Specification<GiftCertificate>
     */
    public static Specification<GiftCertificate> inTags(String... tags) {
        return (root, query, builder) -> {
            Join<GiftCertificate, Tag> join = root.join(GiftCertificate_.tags);
            query.groupBy(root.get(AbstractRepositoryEntity_.id))
                    .having(builder.greaterThanOrEqualTo(builder.count(root.get(AbstractRepositoryEntity_.id)),
                            Long.valueOf(tags.length)));
            return join.get(Tag_.name).in((Object[]) tags);
        };
    }

    /**
     * "Select all gift certificates that are not in the specified tags."
     *
     * @param tags tag names
     * @return Specification<GiftCertificate>
     */
    public static Specification<GiftCertificate> notInTags(String... tags) {
        return (root, query, builder) -> {
            Join<GiftCertificate, Tag> join = root.join(GiftCertificate_.tags, JoinType.LEFT);
            query.groupBy(root.get(AbstractRepositoryEntity_.id))
                    .having(builder.greaterThanOrEqualTo(builder.count(root.get(AbstractRepositoryEntity_.id)),
                            builder.size(root.get(GiftCertificate_.tags)).as(Long.class)));
            return builder.or(builder.not(join.get(Tag_.name).in((Object[]) tags)),
                    builder.isEmpty(root.get(GiftCertificate_.tags)));
        };
    }

    /**
     * Return a Specification that checks if the user id of the Order is equal to the given user id.
     *
     * @param userId The userId to search for
     * @return A Specification object.
     */
    public static Specification<Order> whereUserId(long userId) {
        return (root, query, builder) -> builder.equal(root.get(Order_.user), userId);
    }

    /**
     * Return a specification that will filter orders by userId and orderId.
     *
     * @param userId  The user id to filter by
     * @param orderId The id of the order to be fetched.
     * @return A Specification object.
     */
    public static Specification<Order> whereUserIdAndOrderId(long userId, long orderId) {
        return (root, query, builder) -> builder.and(whereUserId(userId).toPredicate(root, query, builder),
                builder.equal(root.get(AbstractRepositoryEntity_.ID), orderId));
    }
}
