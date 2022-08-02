package com.epam.esm.api.hateoas.assembler.impl;

import com.epam.esm.api.controller.UserController;
import com.epam.esm.api.hateoas.assembler.CollectionModelAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.api.hateoas.CustomLinkRelation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is responsible for converting a list of `OrderDto` objects into a `CollectionModel` object
 */
@Component
public class OrderCollectionAssembler implements CollectionModelAssembler<OrderDto> {

    @Override
    public List<Link> getCollectionLinks(Collection<OrderDto> collection,
                                         SimplePageRequest pageRequest) throws ServiceException {
        long userId = 0;
        Optional<OrderDto> orderDto = collection.stream().findFirst();
        if (orderDto.isPresent()) {
            userId = orderDto.get().getUserId();
        }
        List<Link> links = new ArrayList<>();
        int currentPage = pageRequest.getPage();
        int currentSize = pageRequest.getSize();
        if (currentPage > 1) {
            links.add(linkTo(methodOn(UserController.class)
                    .getUsersOrders(userId, currentPage - 1, currentSize))
                    .withRel(PREVIOUS_PAGE));
        }
        links.add(linkTo(methodOn(UserController.class)
                .getUsersOrders(userId, currentPage, currentSize))
                .withSelfRel());
        links.add(linkTo(methodOn(UserController.class)
                .getUsersOrders(userId, currentPage + 1, currentSize))
                .withRel(NEXT_PAGE));
        links.add(linkTo(methodOn(UserController.class).getUserById(userId))
                .withRel(FIND_USER));
        links.add(linkTo(methodOn(UserController.class).createUserOrder(userId, null))
                .withRel(CREATE_ORDER));
        return links;
    }
}
