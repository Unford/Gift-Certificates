package test.epam.esm.api.controller;

import com.epam.esm.api.config.ApiApplication;
import com.epam.esm.api.controller.GiftCertificateController;
import com.epam.esm.api.hateoas.assembler.impl.GiftCertificateCollectionAssembler;
import com.epam.esm.api.hateoas.processor.GiftCertificateModelProcessor;
import com.epam.esm.api.hateoas.processor.TagModelProcessor;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.service.GiftCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.core.exception.CustomErrorCode.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GiftCertificateController.class)
@ContextConfiguration(classes = {ApiApplication.class})
@Import({GiftCertificateCollectionAssembler.class,
        GiftCertificateModelProcessor.class,
        TagModelProcessor.class})
class GiftCertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GiftCertificateService service;
    private final Class<GiftCertificateController> controllerClass = GiftCertificateController.class;

    private List<GiftCertificateDto> giftCertificateList;

    private final ObjectMapper writer = new ObjectMapper().findAndRegisterModules();

    @BeforeEach
    public void setUp() {
        giftCertificateList = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            GiftCertificateDto giftCertificate = new GiftCertificateDto();
            giftCertificate.setId(i);
            Set<TagDto> tags = new HashSet<>();
            for (long j = i; j <= 3; j++) {
                tags.add(new TagDto(j, "tag " + j));
            }
            giftCertificate.setTags(tags);
            giftCertificate.setCreateDate(LocalDateTime.now().minusHours(i));
            giftCertificate.setLastUpdateDate(LocalDateTime.now().minusMinutes(i));
            giftCertificate.setDescription("description " + i);
            giftCertificate.setName("name " + i);
            giftCertificate.setDuration(Long.valueOf(i).intValue());
            giftCertificate.setPrice(BigDecimal.valueOf(i));
            giftCertificateList.add(giftCertificate);
        }
    }


    @Test
    void givenCertificate_whenCreateCertificate_thenReturn() throws Exception {
        GiftCertificateDto giftCertificate = giftCertificateList.get(0);
        Mockito.when(service.create(Mockito.any())).thenReturn(giftCertificate);

        String uri = linkTo(methodOn(controllerClass).create(giftCertificate))
                .toUriComponentsBuilder()
                .toUriString();


        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(giftCertificate)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(giftCertificate.getId()))
                .andExpect(jsonPath("$.price").value(giftCertificate.getPrice()))
                .andExpect(jsonPath("$.tags", hasSize(giftCertificate.getTags().size())))
                .andExpect(jsonPath("$.tags[*]._links.*", hasSize(2 * giftCertificate.getTags().size())))
                .andExpect(jsonPath("$.createDate").value(giftCertificate.getCreateDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.lastUpdateDate").value(giftCertificate.getLastUpdateDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.description").value(giftCertificate.getDescription()))
                .andExpect(jsonPath("$.duration").value(giftCertificate.getDuration()))
                .andExpect(jsonPath("$._links.*.href").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void givenInvalidCertificate_whenCreateCertificate_thenConstraintViolation() throws Exception {
        GiftCertificateDto giftCertificate = new GiftCertificateDto();
        giftCertificate.setId(-1L);
        Set<TagDto> tagDtos = new HashSet<>();
        tagDtos.add(new TagDto(-1L, ""));
        giftCertificate.setTags(tagDtos);
        giftCertificate.setPrice(BigDecimal.ZERO);
        giftCertificate.setDuration(-1);
        giftCertificate.setDescription("");
        Mockito.when(service.create(Mockito.any())).thenReturn(null);
        CustomErrorCode errorCode = CONSTRAINT_VIOLATION;

        String uri = linkTo(methodOn(controllerClass).create(giftCertificate))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(giftCertificate)))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenCertificates_whenGetAll_thenReturn() throws Exception {
        String[] expectedDescriptions = giftCertificateList
                .stream().map(GiftCertificateDto::getDescription)
                .toArray(String[]::new);
        String[] expectedCreateTime = giftCertificateList
                .stream().map(t -> t.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .toArray(String[]::new);
        String[] expectedUpdateTime = giftCertificateList
                .stream().map(t -> t.getLastUpdateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .toArray(String[]::new);
        Integer[] expectedDurations = giftCertificateList
                .stream().map(GiftCertificateDto::getDuration)
                .toArray(Integer[]::new);

        Mockito.when(service.findAllByParameters(Mockito.any())).thenReturn(giftCertificateList);
        String uri = linkTo(methodOn(controllerClass).getGiftCertificates(null, null))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.certificates", hasSize(giftCertificateList.size())))
                .andExpect(jsonPath("$._embedded.certificates[*].id",
                        containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$._embedded.certificates[*].price",
                        containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$._embedded.certificates[*].tags[*]", hasSize(6)))
                .andExpect(jsonPath("$._embedded.certificates[*].tags[*]._links.*", hasSize(12)))
                .andExpect(jsonPath("$._embedded.certificates[*].createDate",
                        containsInAnyOrder(expectedCreateTime)))
                .andExpect(jsonPath("$._embedded.certificates[*].lastUpdateDate",
                        containsInAnyOrder(expectedUpdateTime)))
                .andExpect(jsonPath("$._embedded.certificates[*].description",
                        containsInAnyOrder(expectedDescriptions)))
                .andExpect(jsonPath("$._embedded.certificates[*].duration",
                        containsInAnyOrder(expectedDurations)))
                .andExpect(jsonPath("$._links.*.href").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).findAllByParameters(Mockito.any());
    }

    @Test
    void givenCertificates_whenGetById_thenReturn() throws Exception {
        GiftCertificateDto giftCertificate = giftCertificateList.get(0);
        Mockito.when(service.findById(Mockito.anyLong())).thenReturn(giftCertificate);
        String uri = linkTo(methodOn(controllerClass).getGiftCertificateById(giftCertificate.getId()))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(giftCertificate.getId()))
                .andExpect(jsonPath("$.price").value(giftCertificate.getPrice()))
                .andExpect(jsonPath("$.tags", hasSize(giftCertificate.getTags().size())))
                .andExpect(jsonPath("$.tags[*]._links.*", hasSize(2 * giftCertificate.getTags().size())))
                .andExpect(jsonPath("$.createDate").value(giftCertificate.getCreateDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.lastUpdateDate").value(giftCertificate.getLastUpdateDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.description").value(giftCertificate.getDescription()))
                .andExpect(jsonPath("$.duration").value(giftCertificate.getDuration()))
                .andExpect(jsonPath("$._links.*.href").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenCertificateNotExist_whenGetById_thenNotFound() throws Exception {
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;
        Mockito.when(service.findById(Mockito.anyLong())).thenThrow(new ServiceException("1", errorCode));
        String uri = linkTo(methodOn(controllerClass).getGiftCertificateById(1))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenCertificate_whenPatchById_thenReturn() throws Exception {
        GiftCertificateDto giftCertificate = giftCertificateList.get(0);
        Mockito.when(service.update(Mockito.any())).thenReturn(giftCertificate);
        String uri = linkTo(methodOn(controllerClass)
                .updateGiftCertificateById(giftCertificate.getId(), giftCertificate))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(giftCertificate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(giftCertificate.getId()))
                .andExpect(jsonPath("$.price").value(giftCertificate.getPrice()))
                .andExpect(jsonPath("$.tags", hasSize(giftCertificate.getTags().size())))
                .andExpect(jsonPath("$.tags[*]._links.*", hasSize(2 * giftCertificate.getTags().size())))
                .andExpect(jsonPath("$.createDate").value(giftCertificate.getCreateDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.lastUpdateDate").value(giftCertificate.getLastUpdateDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.description").value(giftCertificate.getDescription()))
                .andExpect(jsonPath("$.duration").value(giftCertificate.getDuration()))
                .andExpect(jsonPath("$._links.*.href").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    void givenCertificateNotExist_whenPatchById_thenNotFound() throws Exception {
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;
        Mockito.when(service.update(Mockito.any())).thenThrow(new ServiceException("1", errorCode));
        String uri = linkTo(methodOn(controllerClass).updateGiftCertificateById(1, null))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(giftCertificateList.get(0))))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    void givenCertificate_whenDeleteById_thenNoContent() throws Exception {
        Mockito.doNothing().when(service).deleteById(1);
        String uri = linkTo(methodOn(controllerClass).deleteGiftCertificateById(1))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenCertificateNotExist_whenDeleteById_thenNotFound() throws Exception {
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;
        Mockito.doThrow(new ServiceException("1", errorCode)).when(service).deleteById(1);
        String uri = linkTo(methodOn(controllerClass).deleteGiftCertificateById(1))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenOrdersWithCertificate_whenDeleteById_thenConflict() throws Exception {
        CustomErrorCode errorCode = CONFLICT_DELETE;
        Mockito.doThrow(new ServiceException("1", errorCode)).when(service).deleteById(1);
        String uri = linkTo(methodOn(controllerClass).deleteGiftCertificateById(1))
                .toUriComponentsBuilder()
                .toUriString();
        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(service, Mockito.times(1)).deleteById(Mockito.anyLong());
    }


}
