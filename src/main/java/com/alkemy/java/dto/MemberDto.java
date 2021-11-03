package com.alkemy.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotEmpty(message = "{error.member.name}")
    @Pattern(regexp="^[A-Za-z ]*$",message = "{error.member.output}")
    private String name;

    @NotEmpty
    private String facebookUrl;

    @NotEmpty
    private String instagramUrl;

    @NotEmpty
    private String linkedinUrl;

    @NotEmpty(message = "{error.member.image}")
    private String image;

    @NotEmpty
    private String description;

}
