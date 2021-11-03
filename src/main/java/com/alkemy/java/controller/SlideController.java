package com.alkemy.java.controller;

import com.alkemy.java.dto.SlideRequestDto;

import com.alkemy.java.dto.SlideDto;
import com.alkemy.java.service.ISlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alkemy.java.dto.SlideResponseDto;
import com.alkemy.java.util.UtilValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/slides")
public class SlideController {

    @Autowired
    private ISlideService slideService;

    @Value("success.get")
    private String successGet;

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UtilValidation validation;

    @Value("success.deleted")
    private String deletedMessage;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createSlide(@ModelAttribute SlideRequestDto slideRequest) throws Exception {
        
        validation.validationApp(slideRequest);

        return new ResponseEntity<>(slideService.createSlide(slideRequest), HttpStatus.CREATED);

    }
    
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllSlides() {

        List<SlideResponseDto> slides = slideService.getAllSlide();

        if (slides.isEmpty()) {
            String message = messageSource.getMessage(successGet, null, Locale.getDefault());
            return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(slideService.getAllSlide(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        slideService.delete(id);
        return new ResponseEntity<>(messageSource.getMessage(deletedMessage, null, Locale.getDefault()), HttpStatus.OK);

    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getSlide(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(slideService.getById(id));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody SlideDto slideDto) {
        SlideDto slide = slideService.updateSlide(id, slideDto);
        return new ResponseEntity<>(slide, HttpStatus.OK);
    }
}
