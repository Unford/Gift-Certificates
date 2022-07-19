package com.epam.esm.core.service.impl;

import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.core.dao.specification.DaoSpecification;
import com.epam.esm.core.dao.specification.JoinedDaoSpecification;
import com.epam.esm.core.dao.specification.SearchCriteria;
import com.epam.esm.core.dao.specification.SearchOperation;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.GiftCertificate_;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.domain.Tag_;
import com.epam.esm.core.model.dto.GiftCertificateRequest;
import com.epam.esm.core.model.dto.PageRequestParameters;
import com.epam.esm.core.service.GiftCertificateService;
import com.epam.esm.core.util.RequestParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The type Gift certificate service.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDaoImpl certificateDao;
    private final TagDao tagDao;

    private final ModelMapper modelMapper;

    /**
     * Instantiates a new Gift certificate service.
     *
     * @param certificateDao the certificate dao
     * @param tagDao         the tag dao
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDaoImpl certificateDao, TagDao tagDao, ModelMapper modelMapper) {
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
    public List<GiftCertificate> findAll(PageRequestParameters pageRequestParameters) {
        return certificateDao.findAll(RequestParser.convertToPageable(pageRequestParameters));
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
        Specification<GiftCertificate> specification = RequestParser.parseSpecification(pageRequest);
        return certificateDao.findAll(specification, RequestParser.convertToPageable(pageRequest));
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
