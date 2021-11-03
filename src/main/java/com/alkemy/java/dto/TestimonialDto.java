package com.alkemy.java.dto;

import com.alkemy.java.model.Testimonial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestimonialDto {

    @NotEmpty(message = "{testimonial.name.empty}")
    private String name;

    @NotEmpty(message = "{testimonial.image.empty}")
    private String image;

    @NotNull(message = "{testimonial.content.null}")
    private String content;

    public static TestimonialDto testimonialToDto(Testimonial testimonial) {

        return TestimonialDto.builder()
                .name(testimonial.getName())
                .image(testimonial.getImage())
                .content(testimonial.getContent())
                .build();

    }

}
