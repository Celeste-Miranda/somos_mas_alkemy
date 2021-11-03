package com.alkemy.java.dto;

import com.alkemy.java.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private Date creationDate;

    public static UserDtoResponse userToDto(User user) {
        return UserDtoResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getName().toUpperCase())
                .creationDate(user.getCreationDate())
                .build();
    }
}
