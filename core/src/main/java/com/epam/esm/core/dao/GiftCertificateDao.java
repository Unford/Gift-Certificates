package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.GiftCertificate;

import java.util.List;

/**
 * The interface Gift certificate dao.
 */
public interface GiftCertificateDao extends AbstractDao<GiftCertificate> {
    /**
     * Create many to many link boolean.
     *
     * @param giftCertificateId the gift certificate id
     * @param tagId             the tag id
     * @return the boolean
     */
    boolean createManyToManyLink(long giftCertificateId, long tagId);

    /**
     * Is many to many link exist boolean.
     *
     * @param giftCertificateId the gift certificate id
     * @param tagId             the tag id
     * @return the boolean
     */
    boolean isManyToManyLinkExist(long giftCertificateId, long tagId);

    /**
     * Find all by parameters list.
     *
     * @param tag         the tag
     * @param name        the name
     * @param description the description
     * @param sort        the sort
     * @return the list
     */
    List<GiftCertificate> findAllByParameters(String tag, String name, String description, String sort);
}
