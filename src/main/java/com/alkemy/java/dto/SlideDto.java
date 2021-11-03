package com.alkemy.java.dto;

import com.alkemy.java.model.News;
import com.alkemy.java.model.Organization;
import com.alkemy.java.model.Slide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideDto {

    @NotEmpty(message = "{slide.image.empty}")
    private String imageUrl;

    @NotEmpty(message = "{slide.text.empty}")
    private String text;

    @NotNull(message = "{slide.numorder.null}")
    private int numberOrder;

    private Organization organization;

    public static SlideDto slideToDto(Slide slide) {

        return SlideDto.builder()
                .imageUrl(slide.getImageUrl())
                .text(slide.getText())
                .numberOrder(slide.getNumberOrder())
                .organization(slide.getOrganizationId())
                .build();

    }

}
