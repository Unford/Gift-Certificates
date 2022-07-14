package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.GiftCertificate;

import java.util.List;

/**
 * The interface Gift certificate dao.
 */
public interface GiftCertificateDao extends BaseGenericDao<GiftCertificate> {
    /**
     * Find all by parameters list.
     *
     * @param tag         the tag
     * @param name        the name
     * @param description the description
     * @param sort        the sort
     * @return the list
     */
    //todo empty
    List<GiftCertificate> findAllByParameters(String tag, String name, String description, String sort);
}
