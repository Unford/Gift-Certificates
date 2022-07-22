package com.epam.esm.core.service;

import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.request.GiftCertificateRequest;

import java.util.List;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    /**
     * Find all by parameters list.
     *
     * @param pageRequest@return the list
     */
    List<GiftCertificateDto> findAllByParameters(GiftCertificateRequest pageRequest);
}
