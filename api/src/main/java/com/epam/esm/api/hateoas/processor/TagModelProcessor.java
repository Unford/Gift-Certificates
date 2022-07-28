package com.epam.esm.api.hateoas.processor;

import com.epam.esm.api.controller.TagController;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.TagDto;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static com.epam.esm.api.hateoas.CustomLinkRelation.DELETE_BY_ID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TagModelProcessor implements RepresentationModelProcessor<TagDto> {
    @Override
    public TagDto process(TagDto model) {
        try {
            model.add(linkTo(methodOn(TagController.class).getTagById(model.getId()))
                    .withSelfRel());
            model.add(linkTo(methodOn(TagController.class).deleteTagById(model.getId()))
                    .withRel(DELETE_BY_ID));
        } catch (ServiceException ignored) {
        }
        return model;
    }
}
