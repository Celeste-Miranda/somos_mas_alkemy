package com.alkemy.java.service.impl;

import com.alkemy.java.dto.ContactFieldsDto;
import com.alkemy.java.dto.SlidesDto;
import com.alkemy.java.dto.*;
import com.alkemy.java.model.Organization;

import com.alkemy.java.repository.SlideRepository;
import com.alkemy.java.service.IFileService;
import com.alkemy.java.service.IOrganizationService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.alkemy.java.repository.OrganizationRepository;
import com.alkemy.java.dto.OrganizationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Value("error.organization.id.not.found")
    private String idNotFoundMessage;

    @Value("error.email.registered")
    private String errorPath;

    @Override
    public OrganizationDto findById(Long id) throws NotFoundException {

        OrganizationDto response = modelMapper.map(existsVerification(id), OrganizationDto.class);

        List<SlidesDto> slides = slideRepository.findAllByOrganizationId(id)
                .stream()
                .map(SlidesDto::new)
                .collect(Collectors.toList());

        response.setSlides(slides);

        return response;
    }

    @Override
    public OrganizationResponseDto setContactFields(ContactFieldsDto contactFieldsDto, Long idOrg) throws NotFoundException {
        Organization organization = organizationRepository.findById(idOrg)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage(idNotFoundMessage, null, Locale.getDefault())));

        if(contactFieldsDto.getLinkedinUrl() != null)
            organization.setLinkedinUrl(contactFieldsDto.getLinkedinUrl());
        if(contactFieldsDto.getFacebookUrl() != null)
            organization.setFacebookUrl(contactFieldsDto.getFacebookUrl());
        if(contactFieldsDto.getInstagramUrl() != null)
            organization.setInstagramUrl(contactFieldsDto.getInstagramUrl());

        organization.setLastUpdate(new Date());
        Organization orga = organizationRepository.save(organization);
        return OrganizationResponseDto.orgToDto(orga);
    }

    private Organization existsVerification(Long idOrg) throws NotFoundException {
        return organizationRepository.findById(idOrg)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage(idNotFoundMessage, null, Locale.getDefault())));
    }

    @Override
    public OrganizationResponseDto createOrganization(OrganizationRequestDto request) {

        if (organizationRepository.findByEmail(request.getEmail())!= null)
            throw new RuntimeException(messageSource.getMessage(errorPath, null, Locale.getDefault()));

        Organization updatedOrganization =
                OrganizationRequestDto.dtoToOrg(request);

        Organization org = organizationRepository.save(updatedOrganization);

        return OrganizationResponseDto.orgToDto(org);

    }

}
