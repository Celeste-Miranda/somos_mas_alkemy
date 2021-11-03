package com.alkemy.java.controller.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.alkemy.java.util.Constants.*;

public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        final String expired = (String) request.getAttribute("expired");
        Map<String, Object> data = new HashMap<>();

        data.put(MESSAGE, (expired != null) ? UNAUTHORIZED_EXCEPTION_MESSAGE : authException.getMessage());

        data.put(TIMESTAMP, new Date());
        data.put(STATUS_CODE, UNAUTHORIZED_EXCEPTION.value());
        data.put(ERROR, UNAUTHORIZED_EXCEPTION.name());

        response.setStatus(UNAUTHORIZED_EXCEPTION.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(objectMapper.writeValueAsString(data));
    }
}