package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.dto.*;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.service.IOrganizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrganizationController.class)
@Import(Config.class)
@WithMockUser(authorities = "ROLE_ADMIN")
class OrganizationControllerTest {

    @MockBean
    private IOrganizationService organizationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //GET BY ID METHOD
    @Test
    void shouldReturnErrorWhenSendInvalidID() throws Exception {
        when(organizationService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Organization doesn't exist."));
        //then
        mockMvc.perform(get("/organization/public/{id}",anyLong())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Organization doesn't exist."))
                .andDo(print());
    }

    @Test
    void shoulReturnOrganizationWhenSendAnID() throws Exception {
        //given
        OrganizationDto organizationDto = new OrganizationDto("organization", "image", "42157126", "address", "linkedin.url", "facebook.url", "instagram.url", null);
        //when
        when(organizationService.findById(1L)).thenReturn(organizationDto);
        //then
        mockMvc.perform(get("/organization/public/{id}",1L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("organization"))
                .andExpect(jsonPath("$.address").value("address"));
    }
    //UPDATE METHOD
    @Test
    void ShouldReturnIdNotfoundWhenIDNonExistent() throws Exception {
        //given
        ContactFieldsDto contactFieldsDto = new ContactFieldsDto("linkedin.url/hola", "facebook.url/si", "instagram.url/ok");
        //when
        when(organizationService.setContactFields(contactFieldsDto, 1L)).thenThrow(new ResourceNotFoundException("id not found"));
        //then
        mockMvc.perform(patch("/organization/contact/{id}", 1L)
                        .content(objectMapper.writeValueAsString(contactFieldsDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("ResourceNotFoundException"))
                .andExpect(jsonPath("$.message").value("id not found"))
                .andDo(print());
    }

    //UPDATE METHOD
    @Test
    void ShouldUpdateOrganizationWhenCallSetContactFields() throws Exception{
        OrganizationResponseDto responseDto = new OrganizationResponseDto("organization", "address", "image", "42157126", "afsodfkjv123@mail.com", null, "wecome text", "about us", "facebook.url/si", "instagram.url/ok", "linkedin.url/hola");

        ContactFieldsDto contactFieldsDto = new ContactFieldsDto("linkedin.url/hola", "facebook.url/si", "instagram.url/ok");
        doReturn(responseDto).when(organizationService).setContactFields(contactFieldsDto, 1L);
        mockMvc.perform(patch("/organization/contact/{id}", 1L)
                        .content(objectMapper.writeValueAsString(contactFieldsDto))
                        .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.facebookUrl").value("facebook.url/si"))
                    .andExpect(jsonPath("$.instagramUrl").value("instagram.url/ok"))
                    .andExpect(jsonPath("$.linkedinUrl").value("linkedin.url/hola"))
                    .andDo(print());

        }

    //CREATE METHOD
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnOrganizationSavedWhenCreateOrganization() throws Exception {
        //given
        OrganizationResponseDto responseDto = new OrganizationResponseDto("organization", "address", "image", "42157126", "afsodfkjv123@mail.com", null, "wecome text", "about us", "facebook.url", "instagram.url", "linkedin.url");
        OrganizationRequestDto requestDto = new OrganizationRequestDto("organization", "address", "image", "42157126", "afsodfkjv123@mail.com", "wecome text", "about us", "facebook.url", "instagram.url", "linkedin.url");
        //when
        when(organizationService.createOrganization(requestDto)).thenReturn(responseDto);
        //then
        mockMvc.perform(post("/organization/public")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedWhenAnonymousUserCreateOrganization() throws Exception {
        //given
        OrganizationRequestDto requestDto = new OrganizationRequestDto("organization", "address", "image", "42157126", "fulanito@mail.com", "wecome text", "about us", "facebook.url", "instagram.url", "linkedin.url");
        //when
        when(organizationService.createOrganization(requestDto)).thenReturn(new OrganizationResponseDto());
        //then
        mockMvc.perform(post("/organization/public")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnErrorWhenNameIsEmpty() throws Exception {
        //given
        OrganizationRequestDto requestDto = new OrganizationRequestDto("", "address", "image", "42157126", "fulanito@mail.com", "wecome text", "about us", "facebook.url", "instagram.url", "linkedin.url");
        //when
        when(organizationService.createOrganization(requestDto)).thenReturn(new OrganizationResponseDto());
        //then
        mockMvc.perform(post("/organization/public")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("InvalidDataException"))
                .andDo(print());
    }

}