package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.config.MessagesConfig;
import com.alkemy.java.dto.PageDto;
import com.alkemy.java.dto.TestimonialDto;
import com.alkemy.java.dto.TestimonialResponseDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.service.ITestimonialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestimonialController.class)
@Import({Config.class, MessagesConfig.class, MockFilterChain.class})
@WithMockUser(authorities = "ROLE_ADMIN")
class TestimonialControllerTest {

    @MockBean
    private ITestimonialService testimonialService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper mapper;

    private TestimonialDto testimonialDto;

    @BeforeEach
    void setUp() {
        testimonialDto = TestimonialDto.builder().content("content").name("name").image("image").build();
    }

    @AfterEach
    void tearDown() {
        testimonialDto = null;
    }

    @Test
    void validTestimonial_whenCreate_returnObjectCreated() throws Exception{
        TestimonialResponseDto testimonialResponseDto = mapper.map(testimonialDto, TestimonialResponseDto.class);

        doReturn(testimonialResponseDto).when(testimonialService).createTestimonial(testimonialDto);

        mockMvc.perform(post("/testimonials")
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.content", is("content")))
                .andExpect(jsonPath("$.image", is("image")));
    }

    @Test
    void invalidTestimonial_whenCreate_return404() throws Exception{
        TestimonialDto testimonialDto = new TestimonialDto();
        mockMvc.perform(post("/testimonials")
                    .content(objectMapper.writeValueAsString(testimonialDto))
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception",is("InvalidDataException")))
                .andExpect(jsonPath("$.messages", hasSize(3)));
    }

    @Test
    void validId_whenDelete_return200() throws Exception{
        doNothing().when(testimonialService).deleteById(2L);

        mockMvc.perform(delete("/testimonials/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",is("Successfully deleted")))
                .andDo(print());

    }

    @Test
    void nonExistingTestimonial_whenDelete_return404() throws Exception{
        doThrow(new ResourceNotFoundException("testimonial not found")).when(testimonialService).deleteById(10L);

        mockMvc.perform(delete("/testimonials/{id}",10L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", is("testimonial not found")));
    }

    @Test
    void validTestimonial_whenUpdate_return200() throws Exception{
        TestimonialDto testimonialDto2 = TestimonialDto.builder().name("updated name").build();
        when(testimonialService.updateTestimonial(1L, testimonialDto)).thenReturn(testimonialDto2);

        mockMvc.perform(put("/testimonials/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testimonialDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("updated name")));
    }

    @Test
    void nonExistingTestimonial_whenUpdate_return404() throws Exception{

        when(testimonialService.updateTestimonial(10L, testimonialDto)).thenThrow(new ResourceNotFoundException("testimonial not found"));

        mockMvc.perform(put("/testimonials/{id}",10L)
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("testimonial not found")));
    }

    @Test
    void invalidTestimonial_whenUpdate_return400() throws Exception{
        TestimonialDto testimonialDto = new TestimonialDto();

        mockMvc.perform(put("/testimonials/{id}",1)
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*",hasSize(3)));
    }

    @Test
    void validRequest_whenGetAll_return200AndPage() throws Exception{
        PageDto<TestimonialDto> pageDto = new PageDto<>();
        pageDto.setContent(Arrays.asList(testimonialDto, testimonialDto));
        doReturn(pageDto).when(testimonialService).findAll(any(Pageable.class),any(HttpServletRequest.class));
        mockMvc.perform(get("/testimonials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("name")));
    }

    @Test
    @WithAnonymousUser
    void unauthorizedUser_whenGetAll_return401() throws Exception{
        when(testimonialService.findAll(any(Pageable.class),any(HttpServletRequest.class))).thenThrow(new AccessDeniedException("algo"));

        mockMvc.perform(get("/testimonials"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void unauthorizedUser_whenCreate_return401() throws Exception{
        when(testimonialService.createTestimonial(testimonialDto)).thenThrow(new AccessDeniedException("algo"));

        mockMvc.perform(post("/testimonials")
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void unauthorizedUser_whenUpdate_return401() throws Exception{
        when(testimonialService.updateTestimonial(1L, testimonialDto)).thenThrow(new AccessDeniedException("Unauthorized"));

        mockMvc.perform(put("/testimonials/{id}", 1L)
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void unauthorizedUser_whenDelete_return401() throws Exception{
        doThrow(new AccessDeniedException("")).when(testimonialService).deleteById(1L);

        mockMvc.perform(delete("/testimonials/{id}", 1L))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void forbiddenUser_whenGetAll_return403() throws Exception{
        mockMvc.perform(get("/testimonials"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void forbiddenUser_whenCreate_return403() throws Exception{

        mockMvc.perform(post("/testimonials")
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void forbiddenUser_whenUpdate_return403() throws Exception{

        mockMvc.perform(put("/testimonials/{id}", 1L)
                        .content(objectMapper.writeValueAsString(testimonialDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void forbiddenUser_whenDELETE_return403() throws Exception{

        mockMvc.perform(delete("/testimonials/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}