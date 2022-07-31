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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody
                                     @Validated({CreateValidation.class, Default.class})
                                     GiftCertificateDto giftCertificate) throws ServiceException {
        giftCertificate = service.create(giftCertificate);
        return giftCertificate;
    }

    @GetMapping
    public CollectionModel<GiftCertificateDto> getGiftCertificates(@Valid CertificatePageRequest certificatePageRequest,
                                                                   BindingResult result) throws ServiceException {
        List<GiftCertificateDto> giftCertificates = service.findAllByParameters(certificatePageRequest);
        return collectionAssembler.toCollectionModel(giftCertificates, certificatePageRequest);
    }


    @GetMapping("/{id}")
    public GiftCertificateDto getGiftCertificateById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    @PatchMapping("/{id}")
    public GiftCertificateDto updateGiftCertificateById(@PathVariable("id") @Positive long id,
                                                        @RequestBody @Validated({UpdateValidation.class, Default.class})
                                                        GiftCertificateDto giftCertificate)
            throws ServiceException {
        giftCertificate.setId(id);
        return service.update(giftCertificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<GiftCertificate> deleteGiftCertificateById(@PathVariable("id") @Positive long id)
            throws ServiceException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

