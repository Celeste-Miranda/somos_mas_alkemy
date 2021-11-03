package com.alkemy.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactFieldsDto {

    private String linkedinUrl;

    private String facebookUrl;

    private String instagramUrl;
}
