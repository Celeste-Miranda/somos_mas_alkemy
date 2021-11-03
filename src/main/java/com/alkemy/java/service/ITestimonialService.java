package com.alkemy.java.service;

import com.alkemy.java.dto.PageDto;
import com.alkemy.java.dto.TestimonialDto;
import com.alkemy.java.dto.TestimonialResponseDto;


import com.alkemy.java.dto.TestimonialDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ITestimonialService {

    TestimonialResponseDto createTestimonial (TestimonialDto testimonialDto);
    void deleteById(Long id);

    TestimonialDto updateTestimonial(Long id, TestimonialDto testimonialDto);

    PageDto<TestimonialDto> findAll(Pageable page, HttpServletRequest request);
}
