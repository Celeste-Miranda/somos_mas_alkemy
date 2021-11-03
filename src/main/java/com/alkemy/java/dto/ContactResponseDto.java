
package com.alkemy.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponseDto {
     
    
    
    private String name;
    
    private String email;
       
    private String message;
   
    private String phone;
       
    private Date createdDate;
     
    private Date updateDate;

}
