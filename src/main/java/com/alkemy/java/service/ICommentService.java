package com.alkemy.java.service;

import com.alkemy.java.dto.CommentDto;
import com.alkemy.java.model.Comment;

import java.util.List;

import com.alkemy.java.dto.CommentRequestDto;
import com.alkemy.java.dto.CommentResponseDto;

public interface ICommentService {

    void update(Long id, CommentDto commentDto, String token);

    List<CommentResponseDto> getCommentsByNewsId(Long id);

    List<CommentDto> getAllComments();

    void deleteComment(Long id, String token);

    CommentResponseDto createComment(CommentRequestDto commentRequestDto, String token);
}
