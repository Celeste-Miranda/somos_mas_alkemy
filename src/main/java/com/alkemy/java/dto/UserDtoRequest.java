package com.alkemy.java.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class UserDtoRequest {

    @NotEmpty(message = "{error.empty.firstname}")
    @Size(min = 3
            ,max=20,
            message = "{error.size.firstname}")
    private String firstName;

    @NotEmpty(message = "{error.empty.lastName}")
    @Size(min = 3
            ,max=20,
            message = "{error.size.lastname}")
    private String lastName;

    @Email(message = "{error.email.format}")
    @NotEmpty(message = "{error.empty.email}")
    private String email;

    @NotEmpty(message = "{error.empty.password}")
    @Size(min = 8
            ,max=20,
            message = "{error.size.password}")
    private String password;

    @NotNull(message = "Field roleId should not be null or empty")
    private Long roleId;

}