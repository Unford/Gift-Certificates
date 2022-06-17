package com.epam.esm.core.dao.impl;

import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.model.domain.Tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Tag dao.
 */
@Repository
public class TagDaoImpl implements TagDao {//todo jpa


    @Override
    public Tag create(Tag entity) {

        return entity;
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag dao");
    }

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }

    @Override
    public boolean isAnyLinksToTag(long id) {
        return false;
    }
}
