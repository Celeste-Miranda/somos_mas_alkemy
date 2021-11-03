
package com.alkemy.java.service;


import com.alkemy.java.dto.SlideRequestDto;
import com.alkemy.java.dto.SlideResponseCreateDto;
import com.alkemy.java.dto.SlideResponseDto;
import com.alkemy.java.dto.SlideDto;
import java.util.List;




public interface ISlideService {
    
    SlideResponseCreateDto createSlide(SlideRequestDto slideRequest) throws Exception;

    List<SlideResponseDto> getAllSlide();

    SlideResponseDto getById(Long id);

    void delete(Long id);

    SlideDto updateSlide (Long id, SlideDto slideDto);
}
