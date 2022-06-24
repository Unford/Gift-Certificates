package com.epam.esm.core.service.impl;

import com.epam.esm.core.dao.impl.TagDaoImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public List<Tag> findAll() {
        // return tagDao.findAll(2, 3).getContent();//todo
        Page<Tag> tags = tagDao.findAll(2, 3);//todo
        return tags.getContent();
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
    public boolean deleteById(long id) throws ServiceException {
        this.findById(id);
        if (tagDao.isAnyLinksToTag(id)) {
            throw new ServiceException(Long.toString(id), CustomErrorCode.FORBIDDEN_OPERATION);
        }
        return tagDao.deleteById(id);
    }
}
