package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.dto.UserDto;
import com.alkemy.java.dto.UserDtoList;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.User;
import com.alkemy.java.repository.UserRepository;
import com.alkemy.java.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import(Config.class)
@WithMockUser(authorities = "ROLE_ADMIN")
class UserControllerTest {

    @MockBean
    private IUserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenUser_whenPatch_thenReturnUser() throws Exception{
        UserDto user = new UserDto();
        user.setFirstName("name");
        user.setId(2L);

        UserDto userUpdated = new UserDto();
        userUpdated.setFirstName("updated");
        userUpdated.setId(2L);

        doReturn(userUpdated).when(userService).updateUser(user.getId(), user);

        mockMvc.perform(patch("/users/{id}", 2L)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName",is("updated")))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void givenUnauthorizedUser_whenPatch_thenReturn401() throws Exception{
        UserDto userDto = new UserDto();

        mockMvc.perform(patch("/users/{id}", 1L)
                        .content(objectMapper.writeValueAsString(userDto))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                //.andExpect(jsonPath("$.messages",is("Unauthorized")))
                .andDo(print());
    }

    @Test
    void givenUser_whenDelete_thenReturn200() throws Exception{
        when(userService.delete(1L)).thenReturn(new User());
        mockMvc.perform(delete("/users/{id}",1L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void givenNotExistingUser_whenDelete_thenReturn404() throws Exception{
        when(userService.delete(10L)).thenThrow(new ResourceNotFoundException("User not found"));
        mockMvc.perform(delete("/users/{id}",10L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User not found")))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void givenUnauthorizedUser_WhenDelete_thenReturn401() throws Exception{
        mockMvc.perform(delete("/users/list", 1L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                //.andExpect(jsonPath("$.message",is("Unauthorized")))
                .andDo(print());
    }

    @Test
    void given2Users_whenGetAll_thenReturn2UsersArrayJson() throws Exception{
        UserDtoList user1 = new UserDtoList();
        user1.setFirstName("Tom");
        UserDtoList user2 = new UserDtoList();
        user2.setFirstName("Jerry");
        doReturn(Arrays.asList(user1,user2)).when(userService).findAllUsers();

        mockMvc.perform(get("/users/list")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))

        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect( jsonPath("$[0].firstName", is("Tom")))
        .andExpect( jsonPath("$[1].firstName", is("Jerry")))
        .andDo(print());
    }

    @Test
    void givenNoUsers_whenGetAll_thenReturnMessage() throws Exception{
        doReturn(Collections.emptyList()).when(userService).findAllUsers();

        mockMvc.perform(get("/users/list")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string("Get request successful but the list of entities is empty"))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void givenUnauthorizedUser_whenGetAll_thenReturn401() throws Exception{

        mockMvc.perform(get("/users/list")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        //.andExpect(jsonPath("$.message",is("Unauthorized")))
        .andDo(print());

    }
}
