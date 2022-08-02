package com.epam.esm.core.service;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.TagDto;

public interface TagService extends BaseService<TagDto> {
    /**
     * Find the most widely used tag
     *
     * @return The most widely used tag.
     */
    TagDto findTheMostWidelyUsedTag() throws ServiceException;
}
