package com.epam.esm.core.service.impl;

import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.GiftCertificateRequest;
import com.epam.esm.core.model.dto.PageRequest;
import com.epam.esm.core.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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

    private final ModelMapper modelMapper;

    /**
     * Instantiates a new Gift certificate service.
     *
     * @param certificateDao the certificate dao
     * @param tagDao         the tag dao
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, TagDao tagDao, ModelMapper modelMapper) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws ServiceException {
        prepareGiftCertificateTags(giftCertificate);
        GiftCertificate certificate = certificateDao.create(giftCertificate);
        return certificate;

    }

    @Override
    public List<GiftCertificate> findAll(PageRequest pageRequest) {
        return certificateDao.findAll(pageRequest);
    }

    @Override
    public GiftCertificate findById(long id) throws ServiceException {
        return certificateDao.findById(id)
                .orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
    }


    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate entity) throws ServiceException {
        GiftCertificate giftCertificate = findById(entity.getId());
        if (entity.getTags() != null) {
            prepareGiftCertificateTags(entity);
        }
        modelMapper.map(entity, giftCertificate);

        return certificateDao.update(giftCertificate).get();
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        this.findById(id);
        certificateDao.deleteById(id);
    }


    @Override
    public List<GiftCertificate> findAllByParameters(GiftCertificateRequest pageRequest) {
        return this.findAll(pageRequest);//todo
    }

    private GiftCertificate prepareGiftCertificateTags(GiftCertificate giftCertificate) {
        Set<Tag> tags = new HashSet<>();
        for (Tag tag : giftCertificate.getTags()) {
            String name = tag.getName();
            Optional<Tag> optionalTag = tagDao.findByName(name);
            tags.add(optionalTag.orElseGet(() -> {
                tag.setId(null);
                return tag;
            }));
        }
        giftCertificate.setTags(tags);
        return giftCertificate;
    }

}
