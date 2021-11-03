
package com.alkemy.java.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideResponseCreateDto {
    
    private String imageUrl;
   
    private String text;
    
    private int numberOrder;
        
    private Date createDate;
    
    private Date lastUpdate;
}
