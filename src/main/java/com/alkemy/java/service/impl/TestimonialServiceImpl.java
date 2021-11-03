package com.alkemy.java.service.impl;

import com.alkemy.java.dto.PageDto;
import com.alkemy.java.dto.TestimonialDto;
import com.alkemy.java.dto.TestimonialResponseDto;
import com.alkemy.java.model.Testimonial;
import com.alkemy.java.repository.TestimonialRepository;
import com.alkemy.java.service.ITestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import com.alkemy.java.exception.ResourceNotFoundException;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;

@Service
public class TestimonialServiceImpl implements ITestimonialService {

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private ModelMapper mapper;

    @Value("error.testimonial.id.not.found")
    private String idNotFoundMessage;

    @Value("${error.testimonial.page.empty}")
    private String emptyPage;


    @Override
    public void deleteById(Long id) {
        Testimonial slide = testimonialRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageSource.getMessage
                        (idNotFoundMessage, null, Locale.getDefault())));
                   testimonialRepository.delete(slide);
    }

    @Override
    public TestimonialResponseDto createTestimonial(TestimonialDto testimonialDto) {
        
        Testimonial testimonial = mapper.map(testimonialDto,Testimonial.class);
        
        testimonial.setCreateDate(new Date());
        testimonial.setUpdateDate(new Date());
        
        testimonialRepository.save(testimonial);
        
        return mapper.map(testimonial,TestimonialResponseDto.class);
    }
    
    @Override    
    public TestimonialDto updateTestimonial(Long id, TestimonialDto testimonialDto) {
        Testimonial updatedTestimonial = testimonialRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(idNotFoundMessage, null, Locale.getDefault())));

        updatedTestimonial.setName(testimonialDto.getName());
        updatedTestimonial.setImage(testimonialDto.getImage());
        updatedTestimonial.setContent(testimonialDto.getContent());

        testimonialRepository.save(updatedTestimonial);
        return TestimonialDto.testimonialToDto(updatedTestimonial);


    }

    @Override
    public PageDto<TestimonialDto> findAll(Pageable page, HttpServletRequest request) {
        PageDto<TestimonialDto> pageDto = new PageDto<>();
        Map<String,String> links = new HashMap<>();
        List<TestimonialDto> listDtos = new ArrayList<>();
        Page<Testimonial> elements =  testimonialRepository.findAll(page);

        elements.getContent().forEach(element -> listDtos.add(mapper.map(element,TestimonialDto.class)));
        links.put("next",elements.hasNext()?makePaginationLink(request,page.getPageNumber()+1):"");
        links.put("previous",elements.hasPrevious()?makePaginationLink(request,page.getPageNumber()-1):"");

        pageDto.setContent(listDtos);
        pageDto.setLinks(links);

        return pageDto;
    }


    private String makePaginationLink(HttpServletRequest request, int page) {
        return String.format("%s?page=%d", request.getRequestURI(), page);
    }
}
