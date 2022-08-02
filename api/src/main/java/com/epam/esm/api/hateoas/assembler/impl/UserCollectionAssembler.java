package com.epam.esm.api.hateoas.assembler.impl;

import com.epam.esm.api.controller.UserController;
import com.epam.esm.api.hateoas.assembler.CollectionModelAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.epam.esm.api.hateoas.CustomLinkRelation.NEXT_PAGE;
import static com.epam.esm.api.hateoas.CustomLinkRelation.PREVIOUS_PAGE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is responsible for converting a list of `UserDto` objects into a `CollectionModel` of `UserDto` objects
 */
@Component
public class UserCollectionAssembler implements CollectionModelAssembler<UserDto> {
    @Override
    public List<Link> getCollectionLinks(Collection<UserDto> collection,
                                         SimplePageRequest pageRequest) throws ServiceException {
        List<Link> links = new ArrayList<>();
        int currentPage = pageRequest.getPage();
        int currentSize = pageRequest.getSize();
        if (currentPage > 1) {
            links.add(linkTo(methodOn(UserController.class).getUsers(currentPage - 1, currentSize))
                    .withRel(PREVIOUS_PAGE));
        }
        links.add(linkTo(methodOn(UserController.class).getUsers(currentPage, currentSize)).withSelfRel());
        links.add(linkTo(methodOn(UserController.class).getUsers(currentPage + 1, currentSize))
                .withRel(NEXT_PAGE));
        return links;
    }
}
