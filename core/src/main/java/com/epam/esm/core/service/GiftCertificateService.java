package com.epam.esm.core.service;

import com.epam.esm.core.model.domain.GiftCertificate;

import java.util.List;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService extends BasicService<GiftCertificate> {
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
