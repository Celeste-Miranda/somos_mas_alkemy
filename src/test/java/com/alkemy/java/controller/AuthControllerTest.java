package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.config.MessagesConfig;
import com.alkemy.java.dto.*;
import com.alkemy.java.exception.BadRequestException;
import com.alkemy.java.exception.ForbiddenException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.service.IUserService;
import com.alkemy.java.service.impl.UserDetailsServiceImpl;
import com.alkemy.java.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import({Config.class, MessagesConfig.class})

class AuthControllerTest {

    @MockBean
    private IUserService userService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper mapper;


    private AuthenticationRequestDto authRequestDto;
    private UserDtoRequest userDtoRequest;
    private AuthenticationResponseDto authResponseDto;
    private String token;
    private JWTAuthResponse jwtAuthResponse;
    private UserDtoResponse userDtoResponse;


    @BeforeEach
    void setUp() {
        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFAaG90bWFpbC5jb20iLCJleHAiOjE2MzU1NzA2MzAsImlhdCI6MTYzNTUzNDYzMH0.fPgk3XZARKhGs_uqExyva-WKODiuzYto1tzKRIR_9Xg";

        userDtoResponse = new UserDtoResponse();
        userDtoResponse.setFirstName("name");
        userDtoResponse.setLastName("lastname");
        userDtoResponse.setEmail("email@gmail.com");
        userDtoResponse.setRole("USER");
        userDtoResponse.setCreationDate(new Date());

        userDtoRequest = new UserDtoRequest ();
        userDtoRequest.setFirstName("name");
        userDtoRequest.setLastName("lastname");
        userDtoRequest.setEmail("email@gmail.com");
        userDtoRequest.setPassword("123456789");
        userDtoRequest.setRoleId(2L);


    }


    @Test
    void validCreateAuthentication_whenAuthentication_return200() throws Exception {

        authRequestDto = new AuthenticationRequestDto();
        authRequestDto.setEmail("email@gmail.com");
        authRequestDto.setPassword("123456");

        authResponseDto = new AuthenticationResponseDto(token);

       doReturn(authResponseDto).when(userService).createAuthentication(authRequestDto);

        mockMvc.perform(post("/auth/authentication")
                        .content(mapper.writeValueAsString(authRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(token));


    }

    @Test
    void getAuthentication_WithInvalidCredentials_StatusCode400() throws Exception {

       doThrow(new BadRequestException("Incorrect username or password")).when(userService).createAuthentication(new AuthenticationRequestDto());

        mockMvc.perform(post("/auth/authentication")
                        .content(mapper.writeValueAsString(new AuthenticationRequestDto()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Incorrect username or password"));


    }


    @Test
    void validRegister_whenRegisterUser_return202 () throws Exception {



        jwtAuthResponse = new JWTAuthResponse(token,userDtoResponse);

        doReturn(jwtAuthResponse).when(userService).registerUser(userDtoRequest);

        mockMvc.perform(post("/auth/register")
                        .content(mapper.writeValueAsString(userDtoRequest))
                        .contentType(APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));

    }

    @Test
    void badRequest_whenRegister_return400() throws Exception{

        mockMvc.perform(post("/auth/register")
                        .content(mapper.writeValueAsString(new UserDtoRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", hasSize(5)));

    }

    @Test
    void duplicateEmail_whenRegister_return400()throws Exception{

       doThrow(new RuntimeException("Mail is already taken.")).when(userService).registerUser(any(UserDtoRequest.class));

        mockMvc.perform(post("/auth/register")
                        .content(mapper.writeValueAsString(userDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Mail is already taken."));

    }


    @Test
    void getUser_whenGetUserInformation_return200() throws Exception {

        doReturn(userDtoResponse).when(userService).getUserInformation(1l,token);

        mockMvc.perform(get("/auth/{id}",1l)
                        .header("Authorization",token)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("name"));


    }

    @Test
    void idIAndTokenInvalid_whenGetUserInformation_return403() throws Exception {

        doThrow(new ForbiddenException("You do not have the required permissions")).when(userService).getUserInformation(1l,token);

        mockMvc.perform(get("/auth/{id}",1l)
                        .header("Authorization",token)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("You do not have the required permissions"));

    }

    @Test
    void invalidId_whenGetUserInformation_return400() throws Exception{
        doThrow(new ResourceNotFoundException("Id not found")).when(userService).getUserInformation(1l,token);
        mockMvc.perform(get("/auth/{id}",1l)
                        .header("Authorization",token)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Id not found"));

    }

}
