package com.epam.esm.core.service.impl;

import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.PageRequestParameters;
import com.epam.esm.core.service.BaseService;
import com.epam.esm.core.util.RequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements BaseService<Tag> {
    private final TagRepositoryImpl tagRepository;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepository the tag dao
     */
    @Autowired
    public TagServiceImpl(TagRepositoryImpl tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag create(Tag entity) throws ServiceException {
        if (tagRepository.findByName(entity.getName()).isPresent()) {
            throw new ServiceException(entity.getName(), CustomErrorCode.RESOURCE_ALREADY_EXIST);
        }
        return tagRepository.create(entity);
    }

    @Override
    public List<Tag> findAll(PageRequestParameters pageRequestParameters) {
        return tagRepository.findAll(RequestParser.convertToPageable(pageRequestParameters));
    }

    @Override
    public Tag findById(long id) throws ServiceException {
        Optional<Tag> result = tagRepository.findById(id);
        return result.orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
    }


    @Override
    public void deleteById(long id) throws ServiceException {
        Tag tag = this.findById(id);
        if (!tag.getGiftCertificates().isEmpty()) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.CONFLICT_DELETE);
        }
        tagRepository.deleteById(id);
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag service");
    }
}
