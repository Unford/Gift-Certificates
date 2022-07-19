package com.epam.esm.core.dao.impl;

import com.epam.esm.core.dao.AbstractBaseDao;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.model.domain.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The type Tag dao.
 */
@Repository
public class TagDaoImpl extends AbstractBaseDao<Tag> implements TagDao {



    public TagDaoImpl() {
        super(Tag.class);
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag dao");
    }
}
