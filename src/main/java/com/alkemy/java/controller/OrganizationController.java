package com.alkemy.java.controller;

import com.alkemy.java.dto.ContactFieldsDto;
import com.alkemy.java.dto.OrganizationDto;
import com.alkemy.java.dto.OrganizationRequestDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.service.IOrganizationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    IOrganizationService organizationService;


    @PatchMapping("/contact/{id}")
    public ResponseEntity<?> setContactInfo(@RequestBody ContactFieldsDto contactFieldsDto, @PathVariable Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.setContactFields(contactFieldsDto, id));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/public")
    public ResponseEntity<?> createOrganization(@Valid @RequestBody(required = true) OrganizationRequestDto request, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);

        return new ResponseEntity<>(organizationService.createOrganization(request), HttpStatus.CREATED);

    }
}
