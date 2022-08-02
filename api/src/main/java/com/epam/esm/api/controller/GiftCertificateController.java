package com.epam.esm.api.controller;

import com.epam.esm.api.hateoas.assembler.impl.GiftCertificateCollectionAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.request.CertificatePageRequest;
import com.epam.esm.core.service.GiftCertificateService;
import com.epam.esm.core.validation.CreateValidation;
import com.epam.esm.core.validation.UpdateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;
import java.util.List;
/**
 * It's a controller that provides operations on gift certificates
 */
@RestController
@RequestMapping("/certificates")
@Validated
@ExposesResourceFor(GiftCertificateDto.class)
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final GiftCertificateCollectionAssembler collectionAssembler;


    @Autowired
    public GiftCertificateController(GiftCertificateService service,
                                     GiftCertificateCollectionAssembler collectionAssembler) {
        this.service = service;
        this.collectionAssembler = collectionAssembler;
    }

    /**
     * The method creates a new gift certificate and returns it
     *
     * @param certificateDto the object that will be created.
     * @return GiftCertificateDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody
                                     @Validated({CreateValidation.class, Default.class})
                                     GiftCertificateDto certificateDto) throws ServiceException {
        certificateDto = service.create(certificateDto);
        return certificateDto;
    }

    /**
     * It returns a collection of gift certificates, which are filtered by the parameters specified in the request
     *
     * @param certificatePageRequest a request for a page of gift certificates.
     * @param result BindingResult - the result of the validation of the request parameters.
     * @return CollectionModel<GiftCertificateDto>
     */
    @GetMapping
    public CollectionModel<GiftCertificateDto> getGiftCertificates(@Valid CertificatePageRequest certificatePageRequest,
                                                                   BindingResult result) throws ServiceException {
        List<GiftCertificateDto> giftCertificates = service.findAllByParameters(certificatePageRequest);
        return collectionAssembler.toCollectionModel(giftCertificates, certificatePageRequest);
    }


    /**
     * Returns a gift certificate by id
     *
     * @param id the id of the gift certificate
     * @throws ServiceException if gift certificate not found
     * @return GiftCertificateDto
     */
    @GetMapping("/{id}")
    public GiftCertificateDto getGiftCertificateById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    /**
     * It updates a gift certificate by id
     *
     * @param id the id of the gift certificate to be updated
     * @param giftCertificate the object that will be updated.
     * @throws ServiceException if gift certificate not found
     * @return GiftCertificateDto
     */
    @PatchMapping("/{id}")
    public GiftCertificateDto updateGiftCertificateById(@PathVariable("id") @Positive long id,
                                                        @RequestBody @Validated({UpdateValidation.class, Default.class})
                                                        GiftCertificateDto giftCertificate)
            throws ServiceException {
        giftCertificate.setId(id);
        return service.update(giftCertificate);
    }

    /**
     * It deletes a gift certificate by id
     *
     * @param id the id of the gift certificate to be deleted
     * @throws ServiceException if gift certificate not found or has orders
     * @return ResponseEntity<GiftCertificate> no content
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<GiftCertificate> deleteGiftCertificateById(@PathVariable("id") @Positive long id)
            throws ServiceException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

