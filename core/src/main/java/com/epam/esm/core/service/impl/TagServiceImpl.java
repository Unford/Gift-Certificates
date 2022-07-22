package com.epam.esm.core.service.impl;

import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import com.epam.esm.core.service.TagService;
import com.epam.esm.core.util.RequestParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagRepositoryImpl tagRepository;
    private final ModelMapper modelMapper;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepository the tag dao
     * @param modelMapper tag mapper
     */
    @Autowired
    public TagServiceImpl(TagRepositoryImpl tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public TagDto create(TagDto entity) throws ServiceException {
        if (tagRepository.findByName(entity.getName()).isPresent()) {
            throw new ServiceException(entity.getName(), CustomErrorCode.RESOURCE_ALREADY_EXIST);
        }
        Tag newTag = tagRepository.create(modelMapper.map(entity, Tag.class));
        modelMapper.map(newTag, entity);
        return entity;
    }

    @Override
    public List<TagDto> findAll(PageRequestParameters pageRequestParameters) {
        List<Tag> tags = tagRepository.findAll(RequestParser.convertToPageable(pageRequestParameters));
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
    @Transactional
    public void deleteById(long id) throws ServiceException {
        Optional<Tag> result = tagRepository.findById(id);
        Tag tag = result.orElseThrow(() -> new ServiceException(Long.toString(id),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        if (!tag.getGiftCertificates().isEmpty()) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.CONFLICT_DELETE);
        }
        tagRepository.deleteById(id);
    }

    @Override
    public TagDto update(TagDto entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag service");
    }

}
