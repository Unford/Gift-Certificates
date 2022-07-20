package com.epam.esm.core.service.impl;

import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.dto.PageRequestParameters;
import com.epam.esm.core.service.BaseService;
import com.epam.esm.core.util.RequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements BaseService<User> {
    private final UserRepositoryImpl userRepository;


    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> findAll(PageRequestParameters pageRequestParameters) {
        return userRepository.findAll(RequestParser.convertToPageable(pageRequestParameters));
    }

    @Override
    public User findById(long id) throws ServiceException {
        Optional<User> result = userRepository.findById(id);
        return result.orElseThrow(() -> new ServiceException(Long.toString(id), CustomErrorCode.RESOURCE_NOT_FOUND));
    }

    @Override
    public User create(User entity) throws ServiceException {
        throw new UnsupportedOperationException("Create command is forbidden for user service");
    }

    @Override
    public User update(User entity) throws ServiceException {
        throw new UnsupportedOperationException("Update command is forbidden for user service");
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        throw new UnsupportedOperationException("Delete command is forbidden for user service");
    }
}
