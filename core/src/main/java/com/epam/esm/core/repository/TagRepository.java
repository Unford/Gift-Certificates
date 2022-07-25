package com.epam.esm.core.repository;

import com.epam.esm.core.model.domain.Tag;

import java.util.Optional;

/**
 * The interface Tag dao.
 */
public interface TagRepository extends BaseGenericRepository<Tag> {
    Optional<Tag> findTheMostWidelyUsedTag();
}
