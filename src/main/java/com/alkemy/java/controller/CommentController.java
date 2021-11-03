package com.alkemy.java.controller;

import com.alkemy.java.dto.CommentDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.alkemy.java.dto.CommentRequestDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.service.ICommentService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Value("${comment.list.empty}")
    private String commentListEmpty;

    @Autowired
    private MessageSource messageSource;

    @Value("success.deleted")
    private String messageDeleted;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id,
                                           @Valid @RequestBody CommentDto commentDto,
                                           @RequestHeader("Authorization") String token,
                                           BindingResult bindingResult){

        if(bindingResult.hasErrors()) throw new InvalidDataException(bindingResult);

        commentService.update(id, commentDto, token);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getComments(){

        return new ResponseEntity<>(commentService.getAllComments().isEmpty()?
                commentListEmpty:commentService.getAllComments(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id,
                                           @RequestHeader("Authorization") String token) {
        commentService.deleteComment(id, token);
        return new ResponseEntity<>(messageSource.getMessage(messageDeleted, null, Locale.getDefault()), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequestDto commentRequestDto,
                                           @Parameter(hidden = true) BindingResult bindingResult,
                                           @RequestHeader(name = "Authorization", required = true) String token){
        if (bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);

        return new ResponseEntity <>(commentService.createComment(commentRequestDto, token), HttpStatus.CREATED);
    }
}
