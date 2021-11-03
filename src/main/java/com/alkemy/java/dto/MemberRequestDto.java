package com.alkemy.java.dto;

import com.alkemy.java.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {


    @NotEmpty(message = "{error.member.name}")
    @Pattern(regexp="^[A-Za-zñ ]*$",message = "{error.member.output}")
    private String name;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
    @NotEmpty(message = "{error.member.image}")
    private String image;
    private String description;

    public static Member dtoToMember(MemberRequestDto dto) {
        return Member.builder()
                .name(dto.getName())
                .facebookUrl(dto.getFacebookUrl())
                .instagramUrl(dto.getInstagramUrl())
                .linkedinUrl(dto.getLinkedinUrl())
                .deleted(false)
                .image(dto.getImage())
                .description(dto.getDescription())
                .build();
    }
}
