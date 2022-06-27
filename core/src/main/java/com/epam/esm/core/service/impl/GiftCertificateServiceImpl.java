package com.epam.esm.core.service.impl;

import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The type Gift certificate service.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao certificateDao;
    private final TagDao tagDao;

    /**
     * Instantiates a new Gift certificate service.
     *
     * @param certificateDao the certificate dao
     * @param tagDao         the tag dao
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws ServiceException {
        GiftCertificate certificate = certificateDao.create(giftCertificate);
        linkAllTagsToCertificate(certificate.getId(), certificate.getTags());
        return certificate;

    }

    @Override
    public List<GiftCertificate> findAll() {
        return certificateDao.findAll(2, 3);//todo
    }

    @Override
    public GiftCertificate findById(long id) throws ServiceException {
        return certificateDao.findById(id)
                .orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
    }

    @Transactional
    @Override
    public GiftCertificate update(GiftCertificate entity) throws ServiceException {
        findById(entity.getId());
        linkAllTagsToCertificate(entity.getId(), entity.getTags());
        return certificateDao.update(entity).get();
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        this.findById(id);
        certificateDao.deleteById(id);
    }


    @Override
    public List<GiftCertificate> findAllByParameters(String tag, String name, String description, String sort) {
        if (tag == null && name == null && description == null && sort == null) {
            return this.findAll();
        }
        return certificateDao.findAllByParameters(tag, name, description, sort);
    }

    private void linkAllTagsToCertificate(long certificateId, Set<Tag> tagSet){
        for (Tag tag : tagSet) {
            String name = tag.getName();
            Optional<Tag> optionalTag = tagDao.findByName(name);
            if (!optionalTag.isPresent()) {
                tagDao.create(tag);
            } else {
                tag.setId(optionalTag.get().getId());
            }
            if (!certificateDao.isCertificateAssociatedWithTag(certificateId, tag.getId())){
                certificateDao.associateCertificateWithTag(certificateId, tag.getId());
            }
        }
    }

}
