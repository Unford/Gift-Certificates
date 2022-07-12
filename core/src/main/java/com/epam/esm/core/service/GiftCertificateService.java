package com.epam.esm.core.service;

import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.dto.GiftCertificateRequest;

import java.util.List;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService extends BasicService<GiftCertificate> {
    /**
     * Find all by parameters list.
     *
     * @param pageRequest@return the list
     */
    List<GiftCertificate> findAllByParameters(GiftCertificateRequest pageRequest);
}
