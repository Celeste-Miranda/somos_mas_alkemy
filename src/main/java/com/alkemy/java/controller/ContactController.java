package com.alkemy.java.controller;

import com.alkemy.java.dto.ContactListDto;
import com.alkemy.java.dto.ContactRequestDto;
import com.alkemy.java.dto.ContactResponseDto;
import com.alkemy.java.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private IContactService contactService;

    @Autowired
    private MessageSource messageSource;

    @Value("success.get")
    private String successGet;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllContacts() {
        List<ContactListDto> contacts = contactService.getAllContacts();

        if (contacts.isEmpty())
            return new ResponseEntity<>(messageSource.getMessage(successGet, null, Locale.getDefault()), HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createContact(@Valid @RequestBody(required = true) ContactRequestDto contactRequestDto) {
        return new ResponseEntity<>((contactService.createContact(contactRequestDto)), HttpStatus.CREATED);
    }
}
