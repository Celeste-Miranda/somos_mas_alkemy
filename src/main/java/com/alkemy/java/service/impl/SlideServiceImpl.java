package com.alkemy.java.service.impl;

import com.alkemy.java.dto.SlideRequestDto;
import com.alkemy.java.dto.SlideResponseCreateDto;
import com.alkemy.java.repository.OrganizationRepository;
import java.util.Date;
import com.alkemy.java.dto.SlideDto;
import com.alkemy.java.dto.SlideResponseDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.Slide;
import com.alkemy.java.repository.SlideRepository;
import com.alkemy.java.service.IFileService;
import com.alkemy.java.service.ISlideService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
public class SlideServiceImpl implements ISlideService {

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IFileService fileService;
    
    @Value("error.user.notFoundID")
    private String idNotFound;



    @Override
    public SlideResponseCreateDto createSlide(SlideRequestDto slideRequest) throws Exception {
        
        Slide slide = mapToEntity(slideRequest);

        slide.setCreateDate(new Date());
        slide.setLastUpdate(new Date());
        slide.setNumberOrder(validOrderNumber(slideRequest));
        slide.setOrganizationId(organizationRepository.getById(slideRequest.getOrganizationId()));
        slide.setImageUrl(fileService.uploadFile(slideRequest.getFile()));

        slideRepository.save(slide);

        return mapToDto(slide);

    }

    @Override
    public List<SlideResponseDto> getAllSlide() {

        List<Slide> slides = slideRepository.findAll();
        return slides.stream().map(slide -> mapper.map(slide, SlideResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        Slide slide = slideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(idNotFound, null, Locale.getDefault())));
        slideRepository.delete(slide);

    }

    @Override
    @Transactional(readOnly = true)
    public SlideResponseDto getById(Long id) {
        Slide slide = slideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(idNotFound, null, Locale.getDefault())));
        return mapper.map(slide, SlideResponseDto.class);
    }

    @Override
    public SlideDto updateSlide(Long id, SlideDto slideDto) {
        Slide updatedSlide = slideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(idNotFound, null, Locale.getDefault())));

        updatedSlide.setImageUrl(slideDto.getImageUrl());
        updatedSlide.setText(slideDto.getText());
        updatedSlide.setNumberOrder(slideDto.getNumberOrder());
        updatedSlide.setOrganizationId(slideDto.getOrganization());

        slideRepository.save(updatedSlide);
        return SlideDto.slideToDto(updatedSlide);

    }

    private Integer validOrderNumber(SlideRequestDto slideRequest) {
        
        Integer numberOrder = slideRequest.getNumberOrder();
        
        if (slideRequest.getNumberOrder() == null || slideRepository.findByNumberOrder(slideRequest.getNumberOrder()) != null) {

            numberOrder = slideRepository.maxOrder() + 1;
        }
        
        return numberOrder;
    }

    private SlideResponseCreateDto mapToDto(Slide slide) {
        return mapper.map(slide, SlideResponseCreateDto.class);
    }

    private Slide mapToEntity(SlideRequestDto categoryRequest) {

        return mapper.map(categoryRequest, Slide.class);

    }
}
