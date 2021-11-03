package com.alkemy.java.dto;


import com.alkemy.java.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoList {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String photo;

    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Role role;

    private Date creationDate;

    private Date lastUpdate;


}
