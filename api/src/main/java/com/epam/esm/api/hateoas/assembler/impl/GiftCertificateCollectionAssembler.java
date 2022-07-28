package com.epam.esm.api.hateoas.assembler.impl;

import com.epam.esm.api.controller.GiftCertificateController;
import com.epam.esm.api.hateoas.assembler.CollectionModelAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.request.GiftCertificateRequest;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.api.hateoas.CustomLinkRelation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateCollectionAssembler implements CollectionModelAssembler<GiftCertificateDto> {
    private static final String SIZE_PARAMETER = "size";
    private static final String PAGE_PARAMETER = "page";
    private static final String SORT_PARAMETER = "sort";
    private static final String NAME_PARAMETER = "name";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String TAG_PARAMETER = "tag";
    private static final String COMMA_SIGN = ",";

    @Override
    public List<Link> getCollectionLinks(Collection<GiftCertificateDto> collection,
                                         PageRequestParameters pageRequest) throws ServiceException {
        List<Link> links = new ArrayList<>();
        int currentPage = pageRequest.getPage();
        int currentSize = pageRequest.getSize();
        if (currentPage > 1) {
            links.add(Link.of(buildUri(currentSize, currentPage - 1, pageRequest), PREVIOUS_PAGE));
        }
        links.add(Link.of(buildUri(currentSize, currentPage, pageRequest)));
        links.add(Link.of(buildUri(currentSize, currentPage + 1, pageRequest), NEXT_PAGE));

        links.add(linkTo(methodOn(GiftCertificateController.class).create(null)).withRel(CREATE));

        return links;
    }

    private String buildUri(int size, int page, PageRequestParameters requestParameters) throws ServiceException {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(SIZE_PARAMETER, String.valueOf(size));
        parameters.add(PAGE_PARAMETER, String.valueOf(page));
        if (requestParameters.getSort() != null) {
            parameters.add(SORT_PARAMETER, requestParameters.getSort());
        }
        if (requestParameters instanceof GiftCertificateRequest) {
            GiftCertificateRequest giftCertificateRequest = (GiftCertificateRequest) requestParameters;
            List<String> nameParams = giftCertificateRequest.getName();
            if (nameParams != null && !nameParams.isEmpty()) {
                parameters.add(NAME_PARAMETER, String.join(COMMA_SIGN, nameParams));
            }
            List<String> descriptionParams = giftCertificateRequest.getDescription();
            if (descriptionParams != null && !descriptionParams.isEmpty()) {
                parameters.add(DESCRIPTION_PARAMETER, String.join(COMMA_SIGN, descriptionParams));
            }
            if (giftCertificateRequest.getTag() != null) {
                parameters.add(TAG_PARAMETER, giftCertificateRequest.getTag());
            }
        }
        UriComponentsBuilder uriBuilder = linkTo(methodOn(GiftCertificateController.class)
                .getGiftCertificates(null, null))
                .toUriComponentsBuilder();
        parameters.forEach(uriBuilder::queryParam);
        return uriBuilder.build().toUriString();

    }


}
