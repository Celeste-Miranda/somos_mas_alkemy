package com.alkemy.java.dto;


import com.alkemy.java.model.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestDto {

    @NotEmpty(message = "{error.name.empty}")
    @Size(min = 5, max = 50, message = "{error.name.size}")
    private String name;

    @NotEmpty(message = "{error.content.empty}")
    private String content;

    @NotEmpty(message = "{error.image.empty}")
    private String image;

    @NotNull(message = "{error.categoryId.empty}")
    private Integer categoryId;

    public static News dtoToNews(NewsRequestDto dto){
        return News.builder()
                .name(dto.getName())
                .content(dto.getContent())
                .image(dto.getImage())
                .build();
    }
}
