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

/**
 * It's a controller that provides operations on tags
 */
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

    /**
     * > This method returns a TagDto object that is found by the id parameter
     *
     * @param id the id of the tag to be retrieved
     * @throws ServiceException if tag not found
     * @return  TagDto object
     */
    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable("id") @Positive long id) throws ServiceException {
        return service.findById(id);
    }

    /**
     * This method returns a collection of TagDto objects, which are the result of a call to the service layer, which
     * in turn calls the repository layer
     *
     * @param page The page number to return.
     * @param size The number of items to return per page.
     * @return A collection of TagDto objects.
     */
    @GetMapping
    public CollectionModel<TagDto> getTags(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Positive int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive int size)
            throws ServiceException {
        SimplePageRequest simplePage = SimplePageRequest.of(page, size);
        List<TagDto> tags = service.findAll(simplePage);
        return collectionAssembler.toCollectionModel(tags, simplePage);
    }

    /**
     * This method returns the most widely used tag.
     *
     * @return TagDto
     */
    @GetMapping(value = "/the-most-widely")
    public TagDto getTheMostWidelyUsedTag() throws ServiceException {
        return service.findTheMostWidelyUsedTag();
    }

    /**
     * The method creates a new tag and returns it
     *
     * @param tagDto The tag object that will be created.
     * @return TagDto object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@RequestBody @Valid TagDto tagDto) throws ServiceException {
        return service.create(tagDto);
    }

    /**
     * It deletes a tag by id.
     *
     * @param id the id of the tag to be deleted
     * @throws ServiceException if tag not found
     * @return ResponseEntity<TagDto> no content
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<TagDto> deleteTagById(@PathVariable("id") @Positive long id) throws ServiceException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
