package com.alkemy.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotEmpty(message = "{error.comment.body.empty}")
    @Size(max = 255, message = "{error.comment.body.limit}")
    private String body;

}
