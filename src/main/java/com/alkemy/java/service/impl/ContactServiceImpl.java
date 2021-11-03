package com.alkemy.java.service.impl;

import com.alkemy.java.dto.ContactListDto;
import com.alkemy.java.dto.ContactRequestDto;
import com.alkemy.java.dto.ContactResponseDto;
import com.alkemy.java.model.Contact;
import com.alkemy.java.repository.ContactRepository;
import com.alkemy.java.service.IContactService;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alkemy.java.service.IEmailService;
import com.amazonaws.services.kms.model.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;


@Service
@Transactional
public class ContactServiceImpl implements IContactService {
    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IEmailService emailService;

    @Value("error.contact.alreadyexists")
    private String errorContactAlreadyExists;


    @Override
    public ContactResponseDto createContact(ContactRequestDto contactRequest) throws AlreadyExistsException {
        if (contactRepository.findByEmail(contactRequest.getEmail()).isPresent()) {
            throw new AlreadyExistsException(messageSource.getMessage(errorContactAlreadyExists, null, Locale.getDefault()));
        }
        emailService.sendContactEmail(contactRequest.getEmail());

        Contact contact = mapToEntity(contactRequest);
        contact.setCreatedDate(new Date());
        contact.setUpdateDate(new Date());

        contactRepository.save(Contact.fromDtoToContact(contactRequest));

        return mapToDto(contact);
    }

    @Override
    public List<ContactListDto> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return contacts.stream().map( contact -> mapper.map(contact, ContactListDto.class)).collect(Collectors.toList());
    }

    private ContactResponseDto mapToDto(Contact contact) {
        return mapper.map(contact, ContactResponseDto.class);
    }

    private Contact mapToEntity(ContactRequestDto contactRequest) {

        return mapper.map(contactRequest,Contact.class );
    }
    
    
    
}
