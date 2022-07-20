package com.epam.esm.api.controller;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.dto.PageRequestParameters;
import com.epam.esm.core.service.impl.TagServiceImpl;
import com.epam.esm.core.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserServiceImpl service;


    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    /**
     * Gets all tags.
     *
     * @return the all tags
     */
    @GetMapping
    public List<User> getUsers(@Valid PageRequestParameters pageRequestParameters, BindingResult bindingResult) {
        return service.findAll(pageRequestParameters);
    }

}
