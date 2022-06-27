package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.Tag;

/**
 * The interface Tag dao.
 */
public interface TagDao extends BaseGenericDao<Tag> {
    /**
     * Is any links to tag boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean isAnyLinksToTag(long id);
}
