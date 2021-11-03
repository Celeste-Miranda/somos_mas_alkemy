
package com.alkemy.java.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialResponseDto {
    
    
    private String name;

    private String image;

    private String content;
    
    private Date createDate;

    private Date updateDate;


}
