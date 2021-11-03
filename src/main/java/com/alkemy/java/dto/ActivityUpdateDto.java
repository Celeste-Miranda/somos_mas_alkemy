package com.alkemy.java.dto;

import com.alkemy.java.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ActivityUpdateDto {
    private Long id;

    @NotBlank(message = "Field name should not be null or empty")
    private String name;

    @Lob
    @NotBlank (message = "Field content should not be null or empty")
    private String content;

    @NotBlank(message = "Field image should not be null or empty")
    private String image;

    public static ActivityUpdateDto toDto(Activity activity){
        return ActivityUpdateDto.builder()
                .id(activity.getId())
                .content(activity.getContent())
                .image(activity.getImage())
                .name(activity.getName())
                .build();
    }
}
