package com.epam.esm.core.service;

import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.request.GiftCertificateRequest;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    List<GiftCertificateDto> findAllByParameters(GiftCertificateRequest pageRequest);
}
