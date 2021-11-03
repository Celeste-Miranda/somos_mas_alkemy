package com.alkemy.java.dto;

import com.alkemy.java.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;

    private String photo;

    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Role role;

    private Date lastUpdate;
}
