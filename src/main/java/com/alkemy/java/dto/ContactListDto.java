package com.alkemy.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ContactListDto {
    private String name;
    private String email;
    private String message;
    private String phone;
}
