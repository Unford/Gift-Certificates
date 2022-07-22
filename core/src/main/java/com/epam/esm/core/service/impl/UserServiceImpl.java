package com.epam.esm.core.service.impl;

import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import com.epam.esm.core.service.BaseService;
import com.epam.esm.core.util.RequestParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements BaseService<UserDto> {
    private final UserRepositoryImpl userRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<UserDto> findAll(PageRequestParameters pageRequestParameters) {
        return userRepository.findAll(RequestParser.convertToPageable(pageRequestParameters))
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) throws ServiceException {
        Optional<User> result = userRepository.findById(id);
        User user = result.orElseThrow(() -> new ServiceException(Long.toString(id),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto create(UserDto entity) throws ServiceException {
        throw new UnsupportedOperationException("Create command is forbidden for user service");
    }

    @Override
    public UserDto update(UserDto entity) throws ServiceException {
        throw new UnsupportedOperationException("Update command is forbidden for user service");
    }

    @Override
    public void deleteById(long id) throws ServiceException {
        throw new UnsupportedOperationException("Delete command is forbidden for user service");
    }
}
