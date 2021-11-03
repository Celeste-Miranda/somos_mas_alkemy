package com.alkemy.java.dto;

import com.alkemy.java.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "Field first name should not be null or empty!")
    private String body;

    @NotNull(message = "Field first name should not be null or empty!")
    private Long newsID;

    public static Comment dtoToComment(CommentRequestDto commentRequestDto){
        return Comment.builder()
                .body(commentRequestDto.getBody())
                .createDate(new Date())
                .lastUpdate(new Date())
                .build();
    }
}
