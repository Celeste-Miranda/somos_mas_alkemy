package com.alkemy.java.dto;

import com.alkemy.java.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {

    @NotNull(message = "{error.name.empty}")
    @Size(min = 3, max = 50, message = "{error.name.size}")
    private String name;

    @NotEmpty(message = "{error.content.empty}")
    private String content;

    @NotEmpty(message = "{error.image.empty}")
    private String image;

    public static Activity dtoToActivity(ActivityDto dto){
        return Activity.builder()
                .name(dto.getName())
                .content(dto.getContent())
                .image(dto.getImage())
                .createDate(new Date())
                .updateDate(new Date())
                .deleted(false)
                .build();
    }

}
