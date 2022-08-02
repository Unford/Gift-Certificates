package com.epam.esm.api.hateoas.processor;

import com.epam.esm.api.controller.UserController;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.UserDto;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static com.epam.esm.api.hateoas.CustomLinkRelation.CREATE_ORDER;
import static com.epam.esm.api.hateoas.CustomLinkRelation.FIND_USER_ORDERS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is a RepresentationModelProcessor that will be called by Spring when it is building a
 * representation model for a UserDto
 */
@Component
public class UserModelProcessor implements RepresentationModelProcessor<UserDto> {
    @Override
    public UserDto process(UserDto model) {
        try {
            model.add(linkTo(methodOn(UserController.class).getUserById(model.getId())).withSelfRel());
            model.add(linkTo(methodOn(UserController.class).getUsersOrders(model.getId(), 1, 10))
                    .withRel(FIND_USER_ORDERS));
            model.add(linkTo(methodOn(UserController.class).createUserOrder(model.getId(), null))
                    .withRel(CREATE_ORDER));
        } catch (ServiceException ignored) {
        }
        return model;
    }
}
