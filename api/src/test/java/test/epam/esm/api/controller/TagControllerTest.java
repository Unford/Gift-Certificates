package test.epam.esm.api.controller;


import com.epam.esm.api.config.ApiApplication;
import com.epam.esm.api.controller.TagController;
import com.epam.esm.api.hateoas.assembler.impl.TagCollectionAssembler;
import com.epam.esm.api.hateoas.processor.TagModelProcessor;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.esm.core.exception.CustomErrorCode.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
@ContextConfiguration(classes = {ApiApplication.class})
@Import({TagCollectionAssembler.class, TagModelProcessor.class})
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TagService service;

    @Test
    void givenTag_whenFindByIdCall_thenReturn() throws Exception {
        TagDto tag = new TagDto(1L, "tag");
        Mockito.when(service.findById(1L)).thenReturn(tag);

        mockMvc.perform(get("/tags/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("tag"))
                .andExpect(jsonPath("$._links.self.href", notNullValue()));

        Mockito.verify(service, Mockito.times(1)).findById(1L);
    }

    @Test
    void givenNotExistTag_whenFindById_thenResourceNotFound() throws Exception {
        Mockito.when(service.findById(Mockito.anyLong()))
                .thenThrow(new ServiceException(Integer.toString(1), RESOURCE_NOT_FOUND));

        mockMvc.perform(get("/tags/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(RESOURCE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenTag_whenFindByInvalidId_thenConstraintViolation() throws Exception {
        Mockito.when(service.findById(Mockito.anyLong())).thenReturn(null);

        mockMvc.perform(get("/tags/-1"))
                .andDo(print())
                .andExpect(status().is(CONSTRAINT_VIOLATION.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CONSTRAINT_VIOLATION.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    void givenTagList_whenFindAll_thenReturn() throws Exception {
        TagDto tag1 = new TagDto(1L, "tag 1");
        TagDto tag2 = new TagDto(2L, "tag 2");
        TagDto tag3 = new TagDto(3L, "tag 3");

        List<TagDto> tags = Stream.of(tag1, tag2, tag3)
                .collect(Collectors.toList());
        Mockito.when(service.findAll(Mockito.any())).thenReturn(tags);

        mockMvc.perform(get("/tags?size=3&page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tags", hasSize(tags.size())))
                .andExpect(jsonPath("$._embedded.tags[*]._links.*.href", notNullValue()))
                .andExpect(jsonPath("$._links.*.href", notNullValue()));

        Mockito.verify(service, Mockito.times(1)).findAll(Mockito.any());
    }

    @Test
    void givenTagList_whenFindAllByInvalidPageOrSize_thenConstraintViolation() throws Exception {
        TagDto tag1 = new TagDto(1L, "tag 1");
        TagDto tag2 = new TagDto(2L, "tag 2");
        TagDto tag3 = new TagDto(3L, "tag 3");
        List<TagDto> tags = Stream.of(tag1, tag2, tag3)
                .collect(Collectors.toList());
        Mockito.when(service.findAll(Mockito.any())).thenReturn(tags);

        mockMvc.perform(get("/tags?size=-1&page=0"))
                .andDo(print())
                .andExpect(status().is(CONSTRAINT_VIOLATION.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CONSTRAINT_VIOLATION.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(0)).findAll(Mockito.any());
    }

    @Test
    void givenTag_whenFindTheMostUsedOne_thenReturn() throws Exception {
        TagDto tag = new TagDto(1L, "tag 1");
        Mockito.when(service.findTheMostWidelyUsedTag()).thenReturn(tag);
        String uri = linkTo(methodOn(TagController.class).getTheMostWidelyUsedTag())
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag.getId()))
                .andExpect(jsonPath("$.name").value(tag.getName()))
                .andExpect(jsonPath("$._links.*.href", notNullValue()));

        Mockito.verify(service, Mockito.times(1)).findTheMostWidelyUsedTag();
    }

    @Test
    void givenTag_whenDeleteById_thenNoContent() throws Exception {
        Mockito.doNothing().when(service).deleteById(1);
        String uri = linkTo(methodOn(TagController.class).deleteTagById(1))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().isNoContent());
        Mockito.verify(service, Mockito.times(1)).deleteById(1);
    }

    @Test
    void givenTag_whenDeleteByInvalidId_thenConstraintViolation() throws Exception {
        Mockito.doNothing().when(service).deleteById(Mockito.anyLong());
        String uri = linkTo(methodOn(TagController.class).deleteTagById(-1))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().is(CONSTRAINT_VIOLATION.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CONSTRAINT_VIOLATION.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
        Mockito.verify(service, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenTagWithCertificates_whenDeleteById_thenConflict() throws Exception {
        long tagId = 1L;
        Mockito.doThrow(new ServiceException(Long.toString(tagId), CONFLICT_DELETE))
                .when(service).deleteById(Mockito.anyLong());

        String uri = linkTo(methodOn(TagController.class).deleteTagById(tagId))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().is(CONFLICT_DELETE.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CONFLICT_DELETE.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenNotExistTag_whenDeleteById_thenNotFound() throws Exception {
        long tagId = 1L;
        Mockito.doThrow(new ServiceException(Long.toString(tagId), RESOURCE_NOT_FOUND))
                .when(service).deleteById(Mockito.anyLong());

        String uri = linkTo(methodOn(TagController.class).deleteTagById(tagId))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().is(RESOURCE_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(RESOURCE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenAlreadyExistTag_whenCreate_thenAlreadyExist() throws Exception {
        TagDto tag = new TagDto(1L, "tag 1");
        Mockito.when(service.create(Mockito.any()))
                .thenThrow(new ServiceException(tag.getName(), RESOURCE_ALREADY_EXIST));

        String uri = linkTo(methodOn(TagController.class).createTag(null))
                .toUriComponentsBuilder()
                .toUriString();

        ObjectMapper writer = new ObjectMapper();

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(tag)))
                .andDo(print())
                .andExpect(status().is(RESOURCE_ALREADY_EXIST.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(RESOURCE_ALREADY_EXIST.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void givenTag_whenCreate_thenReturnCreated() throws Exception {
        TagDto tag = new TagDto(1L, "tag 1");
        Mockito.when(service.create(Mockito.any())).thenReturn(tag);

        String uri = linkTo(methodOn(TagController.class).createTag(null))
                .toUriComponentsBuilder()
                .toUriString();

        ObjectMapper writer = new ObjectMapper();

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(tag)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(tag.getId()))
                .andExpect(jsonPath("$.name").value(tag.getName()))
                .andExpect(jsonPath("$._links.*.href").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).create(Mockito.any());
    }
}
