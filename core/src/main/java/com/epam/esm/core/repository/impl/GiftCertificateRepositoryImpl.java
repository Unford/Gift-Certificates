package com.epam.esm.core.repository.impl;


import com.epam.esm.core.repository.AbstractBaseRepository;
import com.epam.esm.core.model.domain.GiftCertificate;
import org.springframework.stereotype.Repository;


@Repository
public class GiftCertificateRepositoryImpl extends AbstractBaseRepository<GiftCertificate> {
    public GiftCertificateRepositoryImpl() {
        super(GiftCertificate.class);
    }


}
