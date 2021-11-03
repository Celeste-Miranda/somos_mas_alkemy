package com.alkemy.java.controller;

import com.alkemy.java.dto.TestimonialDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.service.ITestimonialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(value = "Testimonial controller")
@RestController
@RequestMapping("/testimonials")
public class TestimonialController {


    @Autowired
    private ITestimonialService testimonialService;

    @Autowired
    private MessageSource messageSource;

    @Value("success.deleted")
    private String successfullyDeleted;


    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Create a new testimonial")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTestimonial(@Valid @RequestBody TestimonialDto testimonialRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);


        return new ResponseEntity<>(testimonialService.createTestimonial(testimonialRequest), HttpStatus.CREATED);

    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Delete a testimonial")
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTestimonialById(@PathVariable Long id) {
        try {
            testimonialService.deleteById(id);

            return new ResponseEntity<>(messageSource.getMessage
                    (successfullyDeleted, null, Locale.getDefault()),
                    HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Update a testimonial")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody TestimonialDto testimonialDto) {
        TestimonialDto testimonial = testimonialService.updateTestimonial(id, testimonialDto);
        return new ResponseEntity<>(testimonial, HttpStatus.OK);
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Get testimonials")
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTestimonials(@PageableDefault(size = 10) Pageable page, HttpServletRequest request) {
        return new ResponseEntity<>(testimonialService.findAll(page, request), HttpStatus.OK);
    }
}
