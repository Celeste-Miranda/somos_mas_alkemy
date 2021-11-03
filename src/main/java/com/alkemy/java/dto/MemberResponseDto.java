package com.alkemy.java.dto;

import com.alkemy.java.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private String name;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
    private String image;
    private String description;

    public static MemberResponseDto memberToDto(Member member) {
        return MemberResponseDto.builder()
                .name(member.getName())
                .facebookUrl(member.getFacebookUrl())
                .instagramUrl(member.getInstagramUrl())
                .linkedinUrl(member.getLinkedinUrl())
                .image(member.getImage())
                .description(member.getDescription())
                .build();
    }
}
