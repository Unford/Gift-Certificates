package com.epam.esm.core.dao.impl;


import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.model.domain.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * The type Gift certificate dao.
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {//todo jpa


    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        return giftCertificate;
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        return this.findById(entity.getId());
    }

    @Override
    public Page<GiftCertificate> findAll(int pageNumber, int pageSize) {
        return null;//todo
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(long id) {
        return false;
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
