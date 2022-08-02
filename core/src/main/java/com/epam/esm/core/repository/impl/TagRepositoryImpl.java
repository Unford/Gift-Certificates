package com.epam.esm.core.repository.impl;

import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.repository.AbstractBaseRepository;
import com.epam.esm.core.repository.TagRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * It's a repository for tags objects
 */
@Repository
public class TagRepositoryImpl extends AbstractBaseRepository<Tag> implements TagRepository {
    private static final String FIND_THE_MOST_WIDELY_USED_TAG_QUERY = "SELECT tags.* FROM (" +
            "SELECT users.id FROM users " +
            "JOIN orders ON (users.id=orders.user_id) " +
            "GROUP BY users.id " +
            "HAVING SUM(orders.cost) = (" +
                "SELECT MAX(sumOrders.fullCost) FROM (" +
                    "SELECT users.id, Sum(orders.cost) AS fullCost FROM gift_service.users " +
                    "JOIN orders ON (users.id=orders.user_id) " +
                    "GROUP BY users.id) AS sumOrders)" +
                ") AS u " +
            "JOIN orders ON (orders.user_id=u.id) " +
            "JOIN order_has_gift_certificate ON (orders.id = order_has_gift_certificate.order_id) " +
            "JOIN gift_certificate_has_tag ON " +
            "(order_has_gift_certificate.gift_certificate_id=gift_certificate_has_tag.gift_certificate_id) " +
            "JOIN tags ON (tags.id=gift_certificate_has_tag.tag_id) " +
            "GROUP BY (tags.id) " +
            "ORDER BY COUNT(tags.id) DESC " +
            "LIMIT 1";

    public TagRepositoryImpl() {
        super(Tag.class);
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag repository");
    }

    @Override
    public Optional<Tag> findTheMostWidelyUsedTag() {
        return entityManager.createNativeQuery(FIND_THE_MOST_WIDELY_USED_TAG_QUERY, Tag.class)
                .getResultList()
                .stream()
                .findFirst();
    }
}
