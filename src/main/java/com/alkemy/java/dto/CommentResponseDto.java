package com.alkemy.java.dto;

import com.alkemy.java.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private String body;
    private String user;
    private Long news;
    private Date creationDate;
    private Date lastUpdate;

    public static CommentResponseDto commentToDto(Comment comment){
        return CommentResponseDto.builder()
                .body(comment.getBody())
                .user(comment.getUser().getEmail())
                .news(comment.getNews().getId())
                .build();
    }

    public CommentResponseDto(Comment comment) {
        this.body = comment.getBody();
        this.creationDate = comment.getCreateDate();
        this.lastUpdate = comment.getLastUpdate();
    }
}
