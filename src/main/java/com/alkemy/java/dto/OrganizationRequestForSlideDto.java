
package com.alkemy.java.dto;


import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequestForSlideDto {
    
    @NotEmpty (message= "Field id should not be null or empty")
    private Long id;
    
    private String name;
    
    private String image;
    
    private String phone;
    
    private String address;
}
