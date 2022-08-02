package com.epam.esm.api.hateoas.processor;

import com.epam.esm.api.controller.GiftCertificateController;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static com.epam.esm.api.hateoas.CustomLinkRelation.DELETE_BY_ID;
import static com.epam.esm.api.hateoas.CustomLinkRelation.UPDATE_BY_ID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is a RepresentationModelProcessor that will be called by Spring when it is building a
 * representation model for a GiftCertificateDto
 */
@Component
public class GiftCertificateModelProcessor implements RepresentationModelProcessor<GiftCertificateDto> {
    private final TagModelProcessor tagModelProcessor;

    public GiftCertificateModelProcessor(TagModelProcessor tagModelProcessor) {
        this.tagModelProcessor = tagModelProcessor;
    }

    @Override
    public GiftCertificateDto process(GiftCertificateDto model) {
        try {
            model.add(linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(model.getId()))
                    .withSelfRel());
            model.add(linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificateById(model.getId()))
                    .withRel(DELETE_BY_ID));
            model.add(linkTo(methodOn(GiftCertificateController.class)
                    .updateGiftCertificateById(model.getId(), null))
                    .withRel(UPDATE_BY_ID));
            model.getTags().forEach(tagModelProcessor::process);
        } catch (ServiceException ignored) {}
        return model;
    }
}
