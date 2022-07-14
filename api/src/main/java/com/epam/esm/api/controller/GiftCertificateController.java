package com.epam.esm.api.controller;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.dto.GiftCertificateRequest;
import com.epam.esm.core.service.GiftCertificateService;
import com.epam.esm.core.validation.CreateValidation;
import com.epam.esm.core.validation.UpdateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Gift certificate controller.
 */
@RestController
@RequestMapping("/certificates")
@Validated
public class GiftCertificateController {
    private final GiftCertificateService service;

    /**
     * Instantiates a new Gift certificate controller.
     *
     * @param service the service
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * Create response entity.
     *
     * @param giftCertificate the gift certificate
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PostMapping
    public ResponseEntity<GiftCertificate> create(@RequestBody
                                                  @Validated({CreateValidation.class, Default.class})
                                                          GiftCertificate giftCertificate) throws ServiceException {
        giftCertificate = service.create(giftCertificate);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(giftCertificate.getId())
                .toUri();
        return ResponseEntity.created(location).body(giftCertificate);
    }

    @GetMapping
    public List<GiftCertificate> getGiftCertificates(@Valid GiftCertificateRequest giftCertificateRequest,
                                                     BindingResult result) {
        return service.findAllByParameters(giftCertificateRequest);
    }


    /**
     * Gets gift certificate by id.
     *
     * @param id the id
     * @return the gift certificate by id
     * @throws ServiceException the service exception
     */
    @GetMapping("/{id}")
    public GiftCertificate getGiftCertificateById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    /**
     * Update gift certificate by id gift certificate.
     *
     * @param id              the id
     * @param giftCertificate the gift certificate
     * @return the gift certificate
     * @throws ServiceException the service exception
     */
    @PatchMapping("/{id}")
    public GiftCertificate updateGiftCertificateById(@PathVariable("id") @Positive long id, @RequestBody
                                                     @Validated({UpdateValidation.class, Default.class})
                                                             GiftCertificate giftCertificate) throws ServiceException {
        giftCertificate.setId(id);
        return service.update(giftCertificate);
    }

    /**
     * Delete gift certificate by id.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificateById(@PathVariable("id") @Positive long id) throws ServiceException {
        service.deleteById(id);
    }
}

