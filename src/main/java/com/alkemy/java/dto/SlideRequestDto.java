
package com.alkemy.java.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideRequestDto {
    
    @NotNull (message= "Field Image should not be null or empty")
    private MultipartFile file;
    
    @NotBlank (message= "Field Text should not be null or empty")
    private String text;
    
    private Integer numberOrder;
    
    @NotNull (message= "Field Organization should not be null or empty")
    private Long organizationId;

   
}
