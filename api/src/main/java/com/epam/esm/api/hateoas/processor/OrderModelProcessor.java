package com.epam.esm.api.hateoas.processor;

import com.epam.esm.api.controller.UserController;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.OrderDto;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static com.epam.esm.api.hateoas.CustomLinkRelation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is a RepresentationModelProcessor that will be called by Spring when it is building a
 * representation model for a OrderDto
 */
@Component
public class OrderModelProcessor implements RepresentationModelProcessor<OrderDto> {
    @Override
    public OrderDto process(OrderDto model) {
        try {
            model.add(linkTo(methodOn(UserController.class).getUsersOrderById(model.getUserId(), model.getId()))
                    .withSelfRel());
            model.add(linkTo(methodOn(UserController.class).getUserById(model.getUserId()))
                    .withRel(FIND_USER));
        } catch (ServiceException ignored) {
        }
        return model;
    }
}
