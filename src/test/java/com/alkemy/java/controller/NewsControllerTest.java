package com.alkemy.java.controller;

import com.alkemy.java.config.Config;
import com.alkemy.java.config.MessagesConfig;
import com.alkemy.java.dto.CommentResponseDto;
import com.alkemy.java.dto.NewsDto;
import com.alkemy.java.dto.NewsRequestDto;
import com.alkemy.java.dto.NewsResponseDto;
import com.alkemy.java.exception.ConflictException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.Category;
import com.alkemy.java.repository.NewsRepository;
import com.alkemy.java.service.ICommentService;
import com.alkemy.java.service.INewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(NewsController.class)
@Import({Config.class, MessagesConfig.class})
@WithMockUser(authorities = "ROLE_ADMIN")
class NewsControllerTest {

    @MockBean
    private NewsRepository newsRepository;
    @MockBean
    private INewsService newsService;
    @MockBean
    private ICommentService commentService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static NewsRequestDto newsRequestDto;
    private static NewsResponseDto newsResponseDto;
    private static NewsDto newsDto;
    private static NewsDto badNewsDto;
    private static Page<NewsResponseDto> news;
    private static List<CommentResponseDto> comments;
    private static CommentResponseDto comment;

    @BeforeAll
    static void setUp(){
        newsRequestDto = new NewsRequestDto("Política", "Empty", "Url", 1);
        newsResponseDto = new NewsResponseDto("Política", "Empty", "Url");
        newsDto = new NewsDto("Deporte", "Empty", "Url", new Category());
        badNewsDto = new NewsDto("", "", "", null);
        news = new PageImpl(Arrays.asList(newsResponseDto,
                newsResponseDto, newsResponseDto, newsResponseDto));
        comments = new ArrayList<>();
    }

    @Test
    void CreateNews_WithValidNews_StatusCode201() throws Exception{
        doReturn(newsResponseDto).when(newsService).createNews(newsRequestDto);
        mockMvc.perform(post("/news")
                        .content(objectMapper.writeValueAsString(newsRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Política")))
                .andExpect(jsonPath("$.content",is("Empty")))
                .andExpect(jsonPath("$.image", is("Url")))
                .andDo(print());
    }

    @Test
    void CreateNews_WithInvalidNews_StatusCode400() throws Exception{
        mockMvc.perform(post("/news")
                        .content(objectMapper.writeValueAsString(new NewsRequestDto()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void CreateNews_WithEmptyBody_StatusCode400() throws Exception{
        mockMvc.perform(post("/news")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void CreateNews_WithDuplicatedName_StatusCode409() throws Exception{
        doThrow(new ConflictException("Name already exist")).when(newsService).createNews(newsRequestDto);
        mockMvc.perform(post("/news")
                        .content(objectMapper.writeValueAsString(newsRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void CreateNews_WithAnonUser_StatusCode401() throws Exception{
        mockMvc.perform(post("/news")
                        .content(objectMapper.writeValueAsString(newsRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    void CreateNews_WithSimpleUser_StatusCode403() throws Exception{
        mockMvc.perform(post("/news")
                        .content(objectMapper.writeValueAsString(newsRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void DeleteNews_WithValidId_StatusCode200() throws Exception{
        doNothing().when(newsService).deleteNews(1L);
        mockMvc.perform(delete("/news/{id}",1L)
                        .contentType(TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"))
                .andDo(print());
    }

    @Test
    void DeleteNews_WithInvalidId_StatusCode404() throws Exception{
        doThrow(new ResourceNotFoundException("Id not found")).when(newsService).deleteNews(1L);
        mockMvc.perform(delete("/news/{id}", 1L)
                        .contentType(TEXT_PLAIN))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    void DeleteNews_WithSimpleUser_StatusCode403() throws Exception{
        mockMvc.perform(delete("/news/{id}", 1L)
                        .contentType(TEXT_PLAIN))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void DeleteNews_WithAnonUser_StatusCode401() throws Exception{
        mockMvc.perform(delete("/news/{id}", 1L)
                        .contentType(TEXT_PLAIN))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void UpdateNews_WithValidNews_StatusCode200() throws Exception{
        doReturn(newsDto).when(newsService).updateNews(1L, newsDto);
        mockMvc.perform(put("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Deporte")))
                .andExpect(jsonPath("$.content",is("Empty")))
                .andExpect(jsonPath("$.image", is("Url")))
                .andDo(print());
    }

    @Test
    void UpdateNews_WithInvalidNews_StatusCode400() throws Exception{
        mockMvc.perform(put("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(badNewsDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void UpdateNews_WithInvalidId_StatusCode404() throws Exception{
        doThrow(new ResourceNotFoundException("Invalid ID")).when(newsService).updateNews(1L, newsDto);
        mockMvc.perform(put("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void UpdateNews_WithEmptyBody_StatusCode400() throws Exception{
        mockMvc.perform(put("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "USER")
    void UpdateNews_WithSimpleUser_StatusCode403() throws Exception{
        mockMvc.perform(put("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.Error", is("FORBIDDEN")))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void UpdateNews_WithAnonUser_StatusCode401() throws Exception{
        mockMvc.perform(put("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.Error", is("UNAUTHORIZED")))
                .andDo(print());
    }

    @Test
    void GetNewsById_WithValidId_StatusCode200() throws Exception{
        doReturn(newsResponseDto).when(newsService).findNewsById(1L);
        mockMvc.perform(get("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsResponseDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Política")))
                .andExpect(jsonPath("$.content",is("Empty")))
                .andExpect(jsonPath("$.image", is("Url")))
                .andDo(print());
    }

    @Test
    void GetNewsById_WithInvalidId_StatusCode404() throws Exception{
        doThrow(new ResourceNotFoundException("Id not found")).when(newsService).findNewsById(1L);
        mockMvc.perform(get("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsResponseDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Id not found")))
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "USER")
    void GetNewsById_WithSimpleUser_StatusCode403() throws Exception{
        mockMvc.perform(get("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsResponseDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.Message", is("Access is denied. " +
                        "You do not have the required permissions.")))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void GetNewsById_WithAnonUser_StatusCode401() throws Exception{
        mockMvc.perform(get("/news/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newsResponseDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.Message", is("Full authentication is required " +
                        "to access this resource")))
                .andDo(print());
    }

    @Test
    void GetAllNews_WithExistingPage_StatusCode200() throws Exception{
        when(newsService.getNews(any(Pageable.class))).thenReturn(news);
        mockMvc.perform(get("/news")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void GetAllNews_WithNonExistingPage_StatusCode404() throws Exception{
        when(newsService.getNews(any(Pageable.class)))
                .thenThrow(new ResourceNotFoundException("Page doesn't exist."));

        mockMvc.perform(get("/news")
                        .param("page", String.valueOf(2))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Page doesn't exist.")))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void GetAllNews_WithAnonUser_StatusCode401() throws Exception{
        mockMvc.perform(get("/news")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.Message", is("Full authentication is required " +
                        "to access this resource")))
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "USER")
    void GetAllNews_WithSimpleUser_StatusCode200() throws Exception{
        when(newsService.getNews(any(Pageable.class))).thenReturn(news);
        mockMvc.perform(get("/news")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void GetAllComments_WithNotEmptyList_StatusCode200() throws Exception{
        comments.add(comment);
        when(commentService.getCommentsByNewsId(1L)).thenReturn(comments);
        mockMvc.perform(get("/news/{id}/comments",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void GetAllComments_WithEmptyList_StatusCode204() throws Exception{
        comments.clear();
        when(commentService.getCommentsByNewsId(1L)).thenReturn(comments);
        mockMvc.perform(get("/news/{id}/comments",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void GetAllComments_WithInvalidId_StatusCode404() throws Exception{
        when(commentService.getCommentsByNewsId(2L)).thenThrow(new ResourceNotFoundException("ID not found"));
        mockMvc.perform(get("/news/{id}/comments",2L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("ID not found")))
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "USER")
    void GetAllComments_WithSimpleUser_StatusCode200() throws Exception{
        comments.add(comment);
        when(commentService.getCommentsByNewsId(1L)).thenReturn(comments);
        mockMvc.perform(get("/news/{id}/comments",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void GetAllComments_WithAnonUser_StatusCoder401() throws Exception{
        mockMvc.perform(get("/news/{id}/comments",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.Error", is("UNAUTHORIZED")))
                .andExpect(jsonPath("$.Message", is("Full authentication is required " +
                        "to access this resource")))
                .andDo(print());
    }

}