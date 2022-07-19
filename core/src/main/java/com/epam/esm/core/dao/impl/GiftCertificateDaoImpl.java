package com.epam.esm.core.dao.impl;


import com.epam.esm.core.dao.AbstractBaseDao;
import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.model.domain.GiftCertificate;
import org.springframework.stereotype.Repository;


/**
 * The type Gift certificate dao.
 */
@Repository
public class GiftCertificateDaoImpl extends AbstractBaseDao<GiftCertificate> implements GiftCertificateDao {
    public GiftCertificateDaoImpl() {
        super(GiftCertificate.class);
    }



}
