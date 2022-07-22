package com.epam.esm.core.repository.impl;

import com.epam.esm.core.repository.AbstractBaseRepository;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.model.domain.Tag;
import org.springframework.stereotype.Repository;

/**
 * The type Tag dao.
 */
@Repository
public class TagRepositoryImpl extends AbstractBaseRepository<Tag> implements TagRepository {



    public TagRepositoryImpl() {
        super(Tag.class);
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag repository");
    }
}
