package com.epam.esm.core.service.impl;

import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.service.TagService;
import com.epam.esm.core.util.RequestParameterParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is a service that provides methods for working with tags.
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagRepositoryImpl tagRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagRepositoryImpl tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto create(TagDto entity) throws ServiceException {
        if (tagRepository.findByName(entity.getName()).isPresent()) {
            throw new ServiceException(entity.getName(), CustomErrorCode.RESOURCE_ALREADY_EXIST);
        }
        Tag newTag = tagRepository.create(modelMapper.map(entity, Tag.class));
        modelMapper.map(newTag, entity);
        return entity;
    }

    @Override
    public List<TagDto> findAll(SimplePageRequest simplePage) {
        List<Tag> tags = tagRepository.findAll(RequestParameterParser.convertToPageable(simplePage));
        return tags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findById(long id) throws ServiceException {
        Optional<Tag> result = tagRepository.findById(id);
        Tag tag = result.orElseThrow(() -> new ServiceException(Long.toString(id),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        return modelMapper.map(tag, TagDto.class);
    }


    @Override
    public void deleteById(long id) throws ServiceException {
        Optional<Tag> result = tagRepository.findById(id);
        Tag tag = result.orElseThrow(() -> new ServiceException(Long.toString(id),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        Set<GiftCertificate> giftCertificateSet = tag.getGiftCertificates();
        if (giftCertificateSet != null && !giftCertificateSet.isEmpty()) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.CONFLICT_DELETE);
        }
        tagRepository.deleteById(id);
    }

    @Override
    public TagDto update(TagDto entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag service");
    }

    @Override
    public TagDto findTheMostWidelyUsedTag() throws ServiceException {
        Tag tag = tagRepository.findTheMostWidelyUsedTag()
                .orElseThrow(() -> new ServiceException("The most widely used tag",
                        CustomErrorCode.RESOURCE_NOT_FOUND));
        return modelMapper.map(tag, TagDto.class);
    }
}
