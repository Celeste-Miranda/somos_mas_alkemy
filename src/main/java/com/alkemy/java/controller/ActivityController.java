package com.alkemy.java.controller;


import com.alkemy.java.dto.ActivityDto;
import com.alkemy.java.dto.ActivityUpdateDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createActivity(@Valid @RequestBody ActivityDto activityDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);



        return new ResponseEntity<>(activityService.createActivity(activityDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityUpdateDto activityUpdateDto) {
        ActivityUpdateDto activityUpdateDtoResponse = activityService.updateActivity(id, activityUpdateDto);
        return new ResponseEntity<>(activityUpdateDtoResponse, HttpStatus.OK);
    }

}
