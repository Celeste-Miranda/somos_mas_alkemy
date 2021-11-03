package com.alkemy.java.controller;

import com.alkemy.java.dto.CommentResponseDto;
import com.alkemy.java.dto.NewsDto;
import com.alkemy.java.dto.NewsResponseDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.service.ICommentService;
import com.alkemy.java.service.INewsService;

import java.util.List;
import java.util.Locale;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.java.dto.NewsRequestDto;
import com.alkemy.java.dto.PageDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.util.UtilPagination;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@Api(value = "News Controller")
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private INewsService newsService;

    @Autowired
    private ICommentService commentService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UtilPagination utils;

    @Value("success.deleted")
    private String messageDeleted;

    @Value("error.pagination")
    private String paginationError;

    @ApiOperation("Create a new News")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createNews(@Valid @RequestBody NewsRequestDto newsRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(bindingResult);
        }
        return new ResponseEntity<>(newsService.createNews(newsRequestDto), HttpStatus.CREATED);
    }

    @ApiOperation("Delete a News")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable("id") Long id) {
        newsService.deleteNews(id);
        return new ResponseEntity<>(messageSource.getMessage(messageDeleted, null, Locale.getDefault()), HttpStatus.OK);
    }

    @ApiOperation("Update a News")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNews(@PathVariable("id") Long id, @Valid @RequestBody NewsDto newsDto) {
        return new ResponseEntity<>(newsService.updateNews(id, newsDto), HttpStatus.OK);
    }

    @ApiOperation("Get News by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findNewsById(id));
    }

    @ApiOperation("Get all News")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping()
    public ResponseEntity<?> getAllNews(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable,
            @RequestParam(value = "page", defaultValue = "0") int page, HttpServletRequest request) {

        Page<NewsResponseDto> listNews = newsService.getNews(pageable);

        Map<String, String> links = utils.linksPagination(request, listNews);

        if (page >= listNews.getTotalPages())
            throw new ResourceNotFoundException(messageSource.getMessage(paginationError, null, Locale.getDefault()));

        PageDto<NewsResponseDto> response = new PageDto<>();
        response.setContent(listNews.getContent());
        response.setLinks(links);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("Get all Comments by News Id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsByIdNews(@PathVariable Long id){
        List<CommentResponseDto> comments = commentService.getCommentsByNewsId(id);
        return comments.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(comments) : ResponseEntity.ok(comments);
    }

}
