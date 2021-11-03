package com.alkemy.java.dto;

import com.alkemy.java.model.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDtoResponse {

    private String name;

    private String content;

    private String image;

    public static NewsDtoResponse newsToDto(News news) {
        return NewsDtoResponse.builder()
                .name(news.getName())
                .content(news.getContent())
                .image(news.getImage())
                .build();
    }

}
