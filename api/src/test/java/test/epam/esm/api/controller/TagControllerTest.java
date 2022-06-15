package test.epam.esm.api.controller;

import com.epam.esm.api.config.ControllerConfig;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.service.impl.TagServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitWebConfig({ControllerConfig.class, TestConfig.class})
@ActiveProfiles("test")
class TagControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TagServiceImpl mockTagService;

    private List<Tag> tags;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        tags = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tags.add(new Tag(i, "tag_" + i));
        }
    }

    @Test
    void testGetAllTags() throws Exception {
        Mockito.when(mockTagService.findAll()).thenReturn(tags);
        ObjectMapper writer = new ObjectMapper();
        mockMvc.perform(get("/tags/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(writer.writeValueAsString(tags)));
        Mockito.verify(mockTagService, Mockito.times(1)).findAll();
    }

    @Test
    void testGetById() throws Exception {
        Mockito.when(mockTagService.findById(1)).thenReturn(tags.get(0));
        Mockito.when(mockTagService.findById(404))
                .thenThrow(new ServiceException(Long.toString(404), CustomErrorCode.RESOURCE_NOT_FOUND));

        ObjectMapper writer = new ObjectMapper();
        mockMvc.perform(get("/tags/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(writer.writeValueAsString(tags.get(0))));

        mockMvc.perform(get("/tags/s1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.MESSAGE_NOT_READABLE.getCode()))
                .andExpect(jsonPath("$.message").value("Failed to convert value id to long"));

        mockMvc.perform(get("/tags/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.CONSTRAINT_VIOLATION.getCode()));

        mockMvc.perform(get("/tags/404"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.RESOURCE_NOT_FOUND.getCode()));

        Mockito.verify(mockTagService, Mockito.times(1)).findById(1);
        Mockito.verify(mockTagService, Mockito.times(1)).findById(404);

    }

    @Test
    void testCreateTag() throws Exception {
        Tag new_tag = new Tag(55, "new_tag");
        Tag existingTag = new Tag(1, "tag_1");

        Mockito.when(mockTagService.create(Mockito.any()))
                .thenReturn(new_tag)
                .thenThrow(new ServiceException(existingTag.getName(), CustomErrorCode.RESOURCE_ALREADY_EXIST));
        ObjectMapper writer = new ObjectMapper();

        mockMvc.perform(post("/tags/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(new_tag)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(writer.writeValueAsString(new_tag)));

        mockMvc.perform(post("/tags/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(existingTag)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.RESOURCE_ALREADY_EXIST.getCode()));
        Mockito.verify(mockTagService, Mockito.times(2)).create(Mockito.any());

    }

    @Test
    void testDeleteTag() throws Exception {
        Mockito.when(mockTagService.deleteById(Mockito.anyLong()))
                .thenReturn(true)
                .thenThrow(new ServiceException("1", CustomErrorCode.RESOURCE_NOT_FOUND))
                .thenThrow(new ServiceException("1", CustomErrorCode.FORBIDDEN_OPERATION));

        mockMvc.perform(delete("/tags/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/tags/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/tags/2"))
                .andDo(print())
                .andExpect(status().isForbidden());
        Mockito.verify(mockTagService, Mockito.times(3)).deleteById(Mockito.anyLong());
    }


}
