package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.config.MessagesConfig;
import com.alkemy.java.dto.ActivityDto;
import com.alkemy.java.dto.ActivityUpdateDto;
import com.alkemy.java.exception.ConflictException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.repository.ActivityRepository;
import com.alkemy.java.service.IActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ActivityController.class)
@Import({Config.class, MessagesConfig.class})
class ActivityControllerTest {

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private IActivityService service;

    @MockBean
    private ActivityRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private ActivityDto createRequestDto;

    private ActivityDto createResponseDto;

    private ActivityUpdateDto updateRequestDto;
    private ActivityUpdateDto updateResponseDto;

    @BeforeEach
    void setUp() {
        createResponseDto = new ActivityDto(
                "name",
                "content",
                "image"
        );
        createRequestDto = new ActivityDto(
                "name",
                "content",
                "image"
        );
        updateRequestDto = new ActivityUpdateDto(
                1L,
                "name",
                "content",
                "image"
        );
        updateResponseDto = new ActivityUpdateDto(
                1L,
                "updated name",
                "updated content",
                "updated image"
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void createActivityOk() throws Exception {
        doReturn(createResponseDto).when(service).createActivity(createRequestDto);

        mockMvc.perform(post("/activities")
                .content(objectMapper.writeValueAsString(createRequestDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.content", is("content")))
                .andExpect(jsonPath("$.image", is("image")))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void createActivityConflictException() throws Exception {

        when(service.createActivity(createRequestDto)).thenThrow(new ConflictException(""));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void updateActivityOk() throws Exception {
        when(service.updateActivity(1L,updateRequestDto)).
                thenReturn(updateResponseDto);

        mockMvc.perform(put("/activities/{id}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updated name")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("updated content")))
                .andExpect(jsonPath("$.image", is("updated image")))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void updateActivityResourceNotFoundException() throws Exception {

        when(service.updateActivity(1L,updateRequestDto)).thenThrow(new ResourceNotFoundException(""));
    }


}