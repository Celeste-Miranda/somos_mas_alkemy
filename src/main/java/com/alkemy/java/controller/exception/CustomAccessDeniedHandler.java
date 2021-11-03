package com.alkemy.java.controller.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import static com.alkemy.java.util.Constants.*;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {

        Map<String, Object> data = new HashMap<>();
        data.put(TIMESTAMP, new Date());
        data.put(STATUS_CODE, FORBIDDEN_EXCEPTION.value());
        data.put(ERROR, FORBIDDEN_EXCEPTION.name());
        data.put(MESSAGE, exception.getMessage() + FORBIDDEN_EXCEPTION_MESSAGE);

        response.setStatus(FORBIDDEN_EXCEPTION.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(objectMapper.writeValueAsString(data));
    }
}