
package com.alkemy.java.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestDto {
   

    @NotBlank (message = "Field name should not be null or empty")
    private String name;

    @NotBlank(message = "Field email should not be null or empty")
    @Email
    private String email;

    
    @NotBlank(message = "Field message should not be null or empty")
    private String message;

    @NotBlank (message = "Field phone should not be null or empty")
    private String phone;

}
