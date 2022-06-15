package test.epam.esm.api.controller;

import com.epam.esm.api.config.ControllerConfig;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.service.impl.GiftCertificateServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.epam.esm.api.config.TestConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitWebConfig({ControllerConfig.class, TestConfig.class})
@ActiveProfiles("test")
class GiftCertificateControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GiftCertificateServiceImpl mockGiftCertificateService;

    private List<GiftCertificate> giftCertificates;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        giftCertificates = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            LocalDateTime time = LocalDateTime.now().plusHours(i);
            Set<Tag> tagSet = new HashSet<>();
            for (int j = 1; j < i; j++) {
                tagSet.add(new Tag(j, "tag_" + j));
            }
            GiftCertificate certificate = new GiftCertificate.GiftCertificateBuilder()
                    .id(i)
                    .name("gift_certificate_" + i)
                    .description("description_" + i)
                    .price(BigDecimal.valueOf(i))
                    .duration(i)
                    .createDate(time)
                    .lastUpdateDate(time)
                    .tags(tagSet)
                    .build();
            giftCertificates.add(certificate);
        }
    }

    @Test
    void testGetAllGiftCertificates() throws Exception {
        when(mockGiftCertificateService.findAllByParameters(any(), any(), any(), any()))
                .thenReturn(giftCertificates);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/certificates/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(giftCertificates)));

    }

    @Test
    void testGetGiftCertificateById() throws Exception {
        Mockito.when(mockGiftCertificateService.findById(anyLong())).thenReturn(giftCertificates.get(2));
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mockMvc.perform(get("/certificates/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(giftCertificates.get(2))));

        mockMvc.perform(get("/certificates/err1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.MESSAGE_NOT_READABLE.getCode()))
                .andExpect(jsonPath("$.message").value("Failed to convert value id to long"));

        mockMvc.perform(get("/certificates/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.CONSTRAINT_VIOLATION.getCode()));

        verify(mockGiftCertificateService, times(1)).findById(anyLong());
    }

    @Test
    void testCreateGiftCertificate() throws Exception {
        GiftCertificate newGiftCertificate = new GiftCertificate();
        newGiftCertificate.setName("new_GiftCertificate");
        newGiftCertificate.setDescription("new Description");
        newGiftCertificate.setPrice(BigDecimal.ONE);
        newGiftCertificate.setDuration(1);

        LocalDateTime now = LocalDateTime.now();
        GiftCertificate expected = new GiftCertificate.GiftCertificateBuilder()
                .id(6)
                .name("new_GiftCertificate")
                .description("new Description")
                .price(BigDecimal.ONE)
                .duration(1)
                .createDate(now)
                .lastUpdateDate(now)
                .tags(new HashSet<>())
                .build();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        Mockito.when(mockGiftCertificateService.create(any())).thenReturn(expected);

        mockMvc.perform(post("/certificates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newGiftCertificate)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        Mockito.verify(mockGiftCertificateService, times(1)).create(any());
    }

    @Test
    void testValidation() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("new_GiftCertificate");
        certificate.setDescription("new Description");
        certificate.setPrice(BigDecimal.ONE);
        certificate.setDuration(-1);
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mockMvc.perform(post("/certificates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(certificate)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.CONSTRAINT_VIOLATION.getCode()));
        certificate.setDuration(1);
        certificate.setName("shrt");
        mockMvc.perform(post("/certificates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(certificate)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.CONSTRAINT_VIOLATION.getCode()));
    }

    @Test
    void testUpdateGiftCertificate() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("new_GiftCertificate");
        certificate.setDescription("new Description");

        GiftCertificate expected = giftCertificates.get(0);
        expected.setName(certificate.getName());
        expected.setDescription(certificate.getDescription());

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        Mockito.when(mockGiftCertificateService.update(any())).thenReturn(expected);

        mockMvc.perform(patch("/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(certificate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        Mockito.verify(mockGiftCertificateService, times(1)).update(any());

    }

    @Test
    void testDeleteById() throws Exception {
        Mockito.when(mockGiftCertificateService.deleteById(anyLong())).thenReturn(true)
                .thenThrow(new ServiceException("404", CustomErrorCode.RESOURCE_NOT_FOUND));

        mockMvc.perform(delete("/certificates/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/certificates/404"))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(mockGiftCertificateService, Mockito.times(2))
                .deleteById(Mockito.anyLong());

    }

    @Test
    void testFindByParameters() throws Exception {
        List<GiftCertificate> expected = giftCertificates.stream().filter(s -> s.getTags()
                        .stream()
                        .anyMatch(tag -> tag.getName().endsWith("5")))
                .collect(Collectors.toList());

        Mockito.when(mockGiftCertificateService.findAllByParameters(any(), any(), any(), any()))
                .thenReturn(expected);
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        mockMvc.perform(get("/certificates")
                        .param("tag", "555555"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

    }

}
