package com.alkemy.java.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JWTAuthResponse {
    private String accessToken;
    private UserDtoResponse user;

    public JWTAuthResponse(String accessToken,UserDtoResponse user) {
        this.accessToken = accessToken;
        this.user = user;

    }
}
