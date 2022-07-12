package com.epam.esm.api.controller;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.PageRequest;
import com.epam.esm.core.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

/**
 * The type Tag controller.
 */
@RestController
@RequestMapping("/tags")
@Validated
public class TagController {
    private final TagServiceImpl service;

    /**
     * Instantiates a new Tag controller.
     *
     * @param service the service
     */
    @Autowired
    public TagController(TagServiceImpl service) {
        this.service = service;
    }

    /**
     * Gets tag by id.
     *
     * @param id the id
     * @return the tag by id
     * @throws ServiceException the service exception
     */
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    /**
     * Gets all tags.
     *
     * @return the all tags
     */
    @GetMapping
    public List<Tag> getTags(@Valid PageRequest pageRequest, BindingResult bindingResult) {
        return service.findAll(pageRequest);
    }

    /**
     * Create tag response entity.
     *
     * @param tag the tag
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody @Valid Tag tag) throws ServiceException {
        service.create(tag);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tag.getId())
                .toUri();
        return ResponseEntity.created(location).body(tag);
    }

    /**
     * Delete tag by id.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable("id") @Positive long id) throws ServiceException {
        service.deleteById(id);
    }

}
