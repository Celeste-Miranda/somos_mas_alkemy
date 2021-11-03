package com.alkemy.java.service;

import com.alkemy.java.dto.ContactListDto;
import com.alkemy.java.dto.ContactRequestDto;
import com.alkemy.java.dto.ContactResponseDto;
import com.alkemy.java.model.Contact;

import java.util.List;

public interface IContactService {

    ContactResponseDto createContact (ContactRequestDto contactRequestDto);
    List<ContactListDto> getAllContacts();
}
