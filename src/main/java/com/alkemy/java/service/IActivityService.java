package com.alkemy.java.service;

import com.alkemy.java.dto.ActivityDto;
import com.alkemy.java.dto.ActivityUpdateDto;

public interface IActivityService {

    ActivityDto createActivity(ActivityDto activityDto);
    ActivityUpdateDto updateActivity(Long id, ActivityUpdateDto activityUpdateDto);
}
