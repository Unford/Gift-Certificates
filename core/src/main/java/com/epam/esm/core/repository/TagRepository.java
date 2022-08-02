package com.epam.esm.core.repository;

import com.epam.esm.core.model.domain.Tag;

import java.util.Optional;


/**
 * Extending the BaseGenericRepository interface for tag.
 */
public interface TagRepository extends BaseGenericRepository<Tag> {
    /**
     * "Find the tag that has been used the most times."
     *
     * @return Optional<Tag>
     */
    Optional<Tag> findTheMostWidelyUsedTag();
}
