package com.epam.esm.core.service;

import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.request.CertificatePageRequest;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    /**
     * Find all gift certificates by parameters.
     *
     * @param pageRequest certificate page request
     * @return List of GiftCertificateDto objects
     */
    List<GiftCertificateDto> findAllByParameters(CertificatePageRequest pageRequest);
}
