package com.epam.esm.core.dao.impl;


import com.epam.esm.core.dao.AbstractBaseDao;
import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.model.domain.GiftCertificate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * The type Gift certificate dao.
 */
@Repository
public class GiftCertificateDaoImpl extends AbstractBaseDao<GiftCertificate> implements GiftCertificateDao {
    public GiftCertificateDaoImpl() {
        super(GiftCertificate.class);
    }

    @Override
    public boolean associateCertificateWithTag(long giftCertificateId, long tagId) {
        return false;
    }

    @Override
    public boolean isCertificateAssociatedWithTag(long giftCertificateId, long tagId) {
        return false;
    }

    @Override
    public List<GiftCertificate> findAllByParameters(String tag, String name, String description, String sort) {
        return new ArrayList<>();
    }

}
