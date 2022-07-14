package com.epam.esm.core.service.impl;

import com.epam.esm.core.dao.impl.TagDaoImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.PageRequestParameters;
import com.epam.esm.core.service.BasicService;
import com.epam.esm.core.util.RequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements BasicService<Tag> {
    private final TagDaoImpl tagDao;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagDao the tag dao
     */
    @Autowired
    public TagServiceImpl(TagDaoImpl tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag create(Tag entity) throws ServiceException {
        if (tagDao.findByName(entity.getName()).isPresent()) {
            throw new ServiceException(entity.getName(), CustomErrorCode.RESOURCE_ALREADY_EXIST);
        }
        return tagDao.create(entity);
    }

    @Override
    public List<Tag> findAll(PageRequestParameters pageRequestParameters) {
        return tagDao.findAll(RequestParser.convertToPageable(pageRequestParameters));
    }

    @Override
    public Tag findById(long id) throws ServiceException {
        Optional<Tag> result = tagDao.findById(id);
        return result.orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag service");
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        Tag tag = this.findById(id);
        if (!tag.getGiftCertificates().isEmpty()) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.CONFLICT_DELETE);
        }
       tagDao.deleteById(id);
    }
}
