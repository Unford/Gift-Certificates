package com.epam.esm.api.hateoas.assembler.impl;

import com.epam.esm.api.controller.TagController;
import com.epam.esm.api.hateoas.assembler.CollectionModelAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.epam.esm.api.hateoas.CustomLinkRelation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagCollectionAssembler implements CollectionModelAssembler<TagDto> {

    public List<Link> getCollectionLinks(Collection<TagDto> collection, PageRequestParameters pageRequest)
            throws ServiceException {
        List<Link> links = new ArrayList<>();
        int currentPage = pageRequest.getPage();
        int currentSize = pageRequest.getSize();
        if (currentPage > 1) {
            links.add(linkTo(methodOn(TagController.class).getTags(currentPage - 1, currentSize))
                    .withRel(PREVIOUS_PAGE));
        }
        links.add(linkTo(methodOn(TagController.class).getTags(currentPage, currentSize)).withSelfRel());
        links.add(linkTo(methodOn(TagController.class).getTags(currentPage + 1, currentSize))
                .withRel(NEXT_PAGE));

        links.add(linkTo(methodOn(TagController.class).createTag(null))
                .withRel(CREATE));
        links.add(linkTo(methodOn(TagController.class).getTheMostWidelyUsedTag())
                .withRel(THE_MOST_WIDELY_USED_TAG));
        return links;
    }
}
