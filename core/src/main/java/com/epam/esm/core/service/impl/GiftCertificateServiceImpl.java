package com.epam.esm.core.service.impl;

import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.request.CertificatePageRequest;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.service.GiftCertificateService;
import com.epam.esm.core.util.RequestParameterParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepositoryImpl certificateRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepositoryImpl certificateRepository,
                                      TagRepository tagRepository, ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        giftCertificate.setTags(prepareGiftCertificateTags(giftCertificateDto));
        GiftCertificate newCertificate = certificateRepository.create(giftCertificate);
        return modelMapper.map(newCertificate, GiftCertificateDto.class);

    }

    @Override
    public List<GiftCertificateDto> findAll(SimplePageRequest simplePage) {
        List<GiftCertificate> certificates = certificateRepository
                .findAll(RequestParameterParser.convertToPageable(simplePage));
        return certificates.stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto findById(long id) throws ServiceException {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
        return modelMapper.map(certificate, GiftCertificateDto.class);
    }


    @Override
    public GiftCertificateDto update(GiftCertificateDto entity) throws ServiceException {
        GiftCertificate giftCertificate = certificateRepository.findById(entity.getId())
                .orElseThrow(() -> new ServiceException(Long.toString(entity.getId()),
                        CustomErrorCode.RESOURCE_NOT_FOUND));
        modelMapper.map(entity, giftCertificate);
        if (entity.getTags() != null) {
            Set<Tag> tags = prepareGiftCertificateTags(entity);
            giftCertificate.setTags(tags);
        }
        GiftCertificate updatedGiftCertificate = certificateRepository.update(giftCertificate);
        return modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class);
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
        if (!certificate.getOrders().isEmpty()) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.CONFLICT_DELETE);
        }
        certificateRepository.deleteById(id);
    }


    @Override
    public List<GiftCertificateDto> findAllByParameters(CertificatePageRequest pageRequest) {
        Specification<GiftCertificate> specification = RequestParameterParser.parseSpecification(pageRequest);
        return certificateRepository.findAll(specification, RequestParameterParser.convertToPageable(pageRequest))
                .stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    private Set<Tag> prepareGiftCertificateTags(GiftCertificateDto giftCertificate) {
        Set<Tag> tags = new HashSet<>();
        for (TagDto tag : giftCertificate.getTags()) {
            String name = tag.getName();
            Optional<Tag> optionalTag = tagRepository.findByName(name);
            tags.add(optionalTag.orElse(new Tag(name)));
        }
        return tags;
    }

}
