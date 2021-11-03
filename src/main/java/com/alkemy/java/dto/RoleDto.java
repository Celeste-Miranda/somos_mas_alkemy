package com.alkemy.java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    @NotEmpty(message = "Field name should not be null or empty")
    @Size(min = 3, max = 20)
    private String name;
}
