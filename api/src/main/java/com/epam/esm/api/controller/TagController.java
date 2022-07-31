package com.epam.esm.api.controller;

import com.epam.esm.api.hateoas.assembler.impl.TagCollectionAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/tags")
@Validated
@ExposesResourceFor(TagDto.class)
public class TagController {
    private final TagService service;
    private final TagCollectionAssembler collectionAssembler;

    @Autowired
    public TagController(TagService service, TagCollectionAssembler collectionAssembler) {
        this.service = service;
        this.collectionAssembler = collectionAssembler;
    }

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    @GetMapping
    public CollectionModel<TagDto> getTags(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Positive int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive int size)
            throws ServiceException {
        SimplePageRequest simplePage = SimplePageRequest.of(page, size);
        List<TagDto> tags = service.findAll(simplePage);
        return collectionAssembler.toCollectionModel(tags, simplePage);
    }

    @GetMapping(value = "/the-most-widely")
    public TagDto getTheMostWidelyUsedTag() throws ServiceException {
        return service.findTheMostWidelyUsedTag();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@RequestBody @Valid TagDto tag) throws ServiceException {
        return service.create(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<TagDto> deleteTagById(@PathVariable("id") @Positive long id) throws ServiceException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
