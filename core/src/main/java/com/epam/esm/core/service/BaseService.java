package com.epam.esm.core.service;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService<T> {
    T create(T entity) throws ServiceException;

    List<T> findAll(PageRequestParameters pageRequestParameters);

    T findById(long id) throws ServiceException;

    T update(T entity) throws ServiceException;

    void deleteById(long id) throws ServiceException;
}
