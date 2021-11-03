package com.alkemy.java.service;

import com.alkemy.java.dto.UserDtoRequest;

public interface IEmailService {

    public void sendEmailWithTemplate(UserDtoRequest user, String subject);
    public void sendContactEmail(String emailTo);

}
