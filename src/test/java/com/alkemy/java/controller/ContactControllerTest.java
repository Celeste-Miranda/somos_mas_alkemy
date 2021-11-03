package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.config.MessagesConfig;
import com.alkemy.java.dto.ContactListDto;
import com.alkemy.java.dto.ContactRequestDto;
import com.alkemy.java.dto.ContactResponseDto;
import com.alkemy.java.repository.ContactRepository;
import com.alkemy.java.service.IContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
@Import({Config.class, MessagesConfig.class})
@WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
class ContactControllerTest {

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private IContactService contactService;

    @MockBean
    private ContactRepository contactRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private List<ContactListDto> contacts;

    private ContactRequestDto contactRequestDto;

    private ContactResponseDto contactResponseDto;

    // 401, 200, empty
    @Test
    @WithAnonymousUser
    void getAllContactsWithAnonUser401() throws Exception {
        mockMvc.perform(get("/contacts/")
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void getAllContactsWithEmptyList204() throws Exception {
        contacts = new ArrayList<>();
        when(contactService.getAllContacts()).thenReturn(contacts);

        mockMvc.perform(get("/contacts/")
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void getAllContactsCode200() throws Exception {
        contacts = new ArrayList<>();
        contacts.add(new ContactListDto("Juan", "Juan@mail.com", "Prueba", "123"));
        contacts.add(new ContactListDto("Pablo", "Pablo@mail.com", "Prueba", "234"));
        contacts.add(new ContactListDto("Pedro", "Pedro@mail.com", "Prueba", "345"));

        when(contactService.getAllContacts()).thenReturn(contacts);

        mockMvc.perform(get("/contacts/")
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void createContactCodeEmptyValue400() throws Exception {
        contactRequestDto = new ContactRequestDto("", "Jose@mail.com", "Examen", "987");
        when(contactService.createContact(contactRequestDto)).thenReturn(new ContactResponseDto());

        mockMvc.perform(post("/contacts/")
                    .content(objectMapper.writeValueAsString(contactRequestDto))
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());

    }

    @Test
    void createContactCode201() throws Exception {
        contactRequestDto = new ContactRequestDto("Alberto", "Alberto@mail.com", "Evaluacion", "456");
        contactResponseDto = new ContactResponseDto("Alberto", "Alberto@mail.com", "Evaluacion", "456", new Date(), new Date());

       when(contactService.createContact(contactRequestDto)).thenReturn(contactResponseDto);

        mockMvc.perform(post("/contacts/")
                        .content(objectMapper.writeValueAsString(contactRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }


}