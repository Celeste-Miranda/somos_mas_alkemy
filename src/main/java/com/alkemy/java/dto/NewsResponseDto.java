package com.alkemy.java.dto;

import com.alkemy.java.model.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDto {

    private String name;
    private String content;
    private String image;

    public static NewsResponseDto newsToDto(News news) {
        return NewsResponseDto.builder()
                .name(news.getName())
                .content(news.getContent())
                .image(news.getImage())
                .build();
    }

}
