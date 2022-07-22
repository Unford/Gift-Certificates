package com.epam.esm.core.repository.impl;


import com.epam.esm.core.repository.AbstractBaseRepository;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.model.domain.GiftCertificate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * The type Gift certificate dao.
 */
@Repository
public class GiftCertificateRepositoryImpl extends AbstractBaseRepository<GiftCertificate>
        implements GiftCertificateRepository {
    public GiftCertificateRepositoryImpl() {
        super(GiftCertificate.class);
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        GiftCertificate giftCertificate = entityManager.merge(entity);
        entityManager.flush();
        return giftCertificate;
    }
}
