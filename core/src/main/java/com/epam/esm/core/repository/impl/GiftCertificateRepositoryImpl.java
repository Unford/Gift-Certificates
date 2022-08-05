package com.epam.esm.core.repository.impl;


import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.repository.AbstractBaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * It's a repository for GiftCertificate objects
 */
@Repository
public class GiftCertificateRepositoryImpl extends AbstractBaseRepository<GiftCertificate> {
    public GiftCertificateRepositoryImpl() {
        super(GiftCertificate.class);
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate entity) {
        return entityManager.merge(entity);
    }
}
