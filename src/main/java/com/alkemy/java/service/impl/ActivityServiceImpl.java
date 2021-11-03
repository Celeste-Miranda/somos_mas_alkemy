package com.alkemy.java.service.impl;

import com.alkemy.java.dto.ActivityDto;
import com.alkemy.java.dto.ActivityUpdateDto;
import com.alkemy.java.exception.ConflictException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.Activity;
import com.alkemy.java.repository.ActivityRepository;
import com.alkemy.java.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MessageSource messageSource;

    @Value("error.activity.notfound")
    private String messageNotFound;
    
    @Override
    public ActivityDto createActivity(ActivityDto activityDto) {

        if(activityRepository.existsByName(activityDto.getName()))
            throw new ConflictException(messageSource.getMessage("error.activity.already.exist", null, Locale.getDefault()));

        activityRepository.save(ActivityDto.dtoToActivity(activityDto));
        return activityDto;
    }

    @Override
    public ActivityUpdateDto updateActivity(Long id, ActivityUpdateDto activityUpdateDto) {
        Activity updatedActivity = activityRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException(messageSource.getMessage(messageNotFound, null, Locale.getDefault())));

        updatedActivity.setName(activityUpdateDto.getName());
        updatedActivity.setImage(activityUpdateDto.getImage());
        updatedActivity.setContent(activityUpdateDto.getContent());
        updatedActivity.setUpdateDate(new Date());

        updatedActivity = activityRepository.save(updatedActivity);
        return ActivityUpdateDto.toDto(updatedActivity);
    }

}
