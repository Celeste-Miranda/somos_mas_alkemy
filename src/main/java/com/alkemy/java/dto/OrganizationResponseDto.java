package com.alkemy.java.dto;

import com.alkemy.java.model.Category;
import com.alkemy.java.model.Organization;
import com.alkemy.java.model.Slide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationResponseDto {

    private String name;
    private String address;
    private String image;
    private String phone;
    private String email;
    private Date lastUpdate;
    private String welcomeText;
    private String aboutUsText;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;

    public static OrganizationResponseDto orgToDto(Organization organization) {

        return OrganizationResponseDto.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .image(organization.getImage())
                .phone(organization.getPhone())
                .email(organization.getEmail())
                .lastUpdate(organization.getLastUpdate())
                .welcomeText(organization.getWelcomeText())
                .aboutUsText(organization.getAboutUsText())
                .facebookUrl(organization.getFacebookUrl())
                .instagramUrl(organization.getInstagramUrl())
                .linkedinUrl(organization.getLinkedinUrl())
                .build();
    }
}

