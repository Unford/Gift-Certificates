package com.epam.esm.core.service.impl;

import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
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
    private final GiftCertificateRepositoryImpl certificateRepository;
    private final TagRepository tagRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepositoryImpl certificateRepository, TagRepository tagRepository, ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws ServiceException {
        prepareGiftCertificateTags(giftCertificate);
        GiftCertificate certificate = certificateRepository.create(giftCertificate);
        return certificate;

    }

    @Override
    public List<GiftCertificate> findAll(PageRequestParameters pageRequestParameters) {
        return certificateRepository.findAll(RequestParser.convertToPageable(pageRequestParameters));
    }

    @Override
    public GiftCertificate findById(long id) throws ServiceException {
        return certificateRepository.findById(id)
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

        return certificateRepository.update(giftCertificate).get();
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        this.findById(id);
        certificateRepository.deleteById(id);
    }


    @Override
    public List<GiftCertificate> findAllByParameters(GiftCertificateRequest pageRequest) {
        Specification<GiftCertificate> specification = RequestParser.parseSpecification(pageRequest);
        return certificateRepository.findAll(specification, RequestParser.convertToPageable(pageRequest));
    }

    private GiftCertificate prepareGiftCertificateTags(GiftCertificate giftCertificate) {
        Set<Tag> tags = new HashSet<>();
        for (Tag tag : giftCertificate.getTags()) {
            String name = tag.getName();
            Optional<Tag> optionalTag = tagRepository.findByName(name);
            tags.add(optionalTag.orElseGet(() -> {
                tag.setId(null);
                return tag;
            }));
        }
        giftCertificate.setTags(tags);
        return giftCertificate;
    }

}
