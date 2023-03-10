package com.epam.esm.core.service.impl;

import com.epam.esm.core.model.domain.Order;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.core.util.RequestParameterParser.*;

/**
 * This class is a service that provides methods for working with gift certificates.
 */
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
                .findAll(convertToPageable(simplePage));
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
        setAllNotNullFields(entity, giftCertificate);
        GiftCertificate updatedGiftCertificate = certificateRepository.update(giftCertificate);
        return modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class);
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
        Set<Order> orders = certificate.getOrders();
        if (orders != null && !orders.isEmpty()) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.CONFLICT_DELETE);
        }
        certificateRepository.deleteById(id);
    }


    @Override
    public List<GiftCertificateDto> findAllByParameters(CertificatePageRequest pageRequest) {
        Specification<GiftCertificate> specification = parseCertificateSpecification(pageRequest);
        return certificateRepository.findAll(specification, convertToPageable(pageRequest))
                .stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    /**
     * It takes a DTO and a GiftCertificate entity, and sets all the fields of the entity that are not null in the DTO
     *
     * @param source the object from which the data is taken
     * @param destination the object to which the values will be copied
     */
    private void setAllNotNullFields(GiftCertificateDto source, GiftCertificate destination) {
        if (source.getName() != null) {
            destination.setName(source.getName());
        }
        if (source.getDescription() != null) {
            destination.setDescription(source.getDescription());
        }
        if (source.getDuration() != null) {
            destination.setDuration(source.getDuration());
        }
        if (source.getPrice() != null) {
            destination.setPrice(source.getPrice());
        }
        if (source.getTags() != null) {
            Set<Tag> tags = prepareGiftCertificateTags(source);
            destination.setTags(tags);
        }
    }

    /**
     * If the tag exists in the database, add it to the set of tags,
     * otherwise create a new tag and add it to the set of tags.
     *
     * @param giftCertificate the GiftCertificateDto.
     * @return Set of tags
     */
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
