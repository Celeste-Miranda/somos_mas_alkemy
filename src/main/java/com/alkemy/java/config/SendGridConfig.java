package com.alkemy.java.config;

import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:sendgrid.properties")
public class SendGridConfig {

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid("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");
    }

}