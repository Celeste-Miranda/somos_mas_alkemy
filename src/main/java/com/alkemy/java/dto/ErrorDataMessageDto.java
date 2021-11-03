
package com.alkemy.java.dto;


import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDataMessageDto {
    
    private Date timestamp;
    private String exception;
    private List <String> messages;


}
