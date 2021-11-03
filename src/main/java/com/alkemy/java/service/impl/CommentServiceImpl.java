package com.alkemy.java.service.impl;

import com.alkemy.java.dto.CommentDto;
import com.alkemy.java.exception.BadRequestException;
import com.alkemy.java.exception.ForbiddenException;
import com.alkemy.java.model.Comment;
import com.alkemy.java.dto.CommentRequestDto;
import com.alkemy.java.dto.CommentResponseDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.News;
import com.alkemy.java.model.User;
import com.alkemy.java.repository.CommentRepository;
import com.alkemy.java.repository.NewsRepository;
import com.alkemy.java.repository.UserRepository;
import com.alkemy.java.service.ICommentService;
import com.alkemy.java.service.IUserService;
import org.modelmapper.ModelMapper;
import com.alkemy.java.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private IUserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Value("error.member.idNotFound")
    private String messageNotFound;

    @Autowired
    ModelMapper mapper;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, String token){

        News news = newsRepository.findById(commentRequestDto.getNewsID()).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(messageNotFound, null, Locale.getDefault())));

        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email);

        Comment comment = CommentRequestDto.dtoToComment(commentRequestDto);
        comment.setUser(user);
        comment.setNews(news);

        Comment finalComment = commentRepository.save(comment);
        return CommentResponseDto.commentToDto(finalComment);
    }

    @Override
    public void update(Long id, CommentDto commentDto, String token) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () -> new BadRequestException(messageSource.getMessage("error.comment.invalid.id",null, Locale.getDefault())));

        if(!userService.validedRole(comment.getUser().getId(),token)) throw new ForbiddenException(messageSource.getMessage("error.user.forbidden",null,Locale.getDefault()));

        comment.setBody(commentDto.getBody());
        comment.setLastUpdate(new Date());
        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByNewsId(Long id) {
        return commentRepository.getCommentsByIdNews(id)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<CommentDto> dtoComments = new ArrayList<>();
        commentRepository.findAllByOrder().forEach(comment ->dtoComments.add(mapper.map(comment,CommentDto.class)));

        return dtoComments;

    }

    @Override
    public void deleteComment(Long id, String token) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () -> new BadRequestException(messageSource.getMessage("error.comment.invalid.id",null, Locale.getDefault())));

        if(!userService.validedRole(comment.getUser().getId(),token))
            throw new ForbiddenException(messageSource.getMessage("error.user.forbidden",null,Locale.getDefault()));

        commentRepository.delete(comment);
    }
}
