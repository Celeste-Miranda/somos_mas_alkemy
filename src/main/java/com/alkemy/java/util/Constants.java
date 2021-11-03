package com.alkemy.java.util;

import org.springframework.http.HttpStatus;

public class Constants {

    //EXCEPTIONS
    public final static String USERNAME_NOT_FOUND = "UsernameNotFoundException";
    public final static String NOT_FOUND = "NotFoundException";
    public final static String INVALID_ROL = "errorInvalidRol";
    public final static String EMAIL_NOT_SENT = "EmailNotSentException";
    public final static String INVALID_DATA = "InvalidDataException";
    public final static String BAD_REQUEST = "BadRequestException";
    public final static String FORBIDDEN = "ForbiddenException";
    public final static String CONFLICT = "ConflictException";
    public final static String NOT_READABLE = "HttpMessageNotReadableException";
    public final static String ARGUMENT_TYPE_MISMATCH = "ArgumentTypeMismatchException";
    public final static String CONSTRAINT_VIOLATION = "ConstraintViolationException";

    public final static HttpStatus UNAUTHORIZED_EXCEPTION = HttpStatus.UNAUTHORIZED;
    public final static HttpStatus FORBIDDEN_EXCEPTION = HttpStatus.FORBIDDEN;
    public final static String FORBIDDEN_EXCEPTION_MESSAGE = ". You do not have the required permissions.";
    public final static String UNAUTHORIZED_EXCEPTION_MESSAGE = "Token expired";

    public final static String TIMESTAMP = "Timestamp";
    public final static String ERROR = "Error";
    public final static String STATUS_CODE = "Status code";
    public final static String MESSAGE = "Message";

    //OTHERS
    public static final String SPACE = " ";

    //ROLES
    public final static String ROLE_USER = "ROLE_USER";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";


    // SWAGGER
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public static final String JWT = "JWT";
    public static final String HEADER = "HEADER";
    public static final String PATH = "com.alkemy.java.controller";

    public static final String TITLE = "API REST Somos más";
    public static final String VERSION = "v1";
    public static final String DESCRIPTION = "Somos más API REST";
    public static final String TERMS_OF_SERVICE = "Términos y condiciones";

    public static final String CONTACT_NAME = "ONG Somos más";
    public static final String CONTACT_URL = "www.somosmas.com.ar";
    public static final String CONTACT_EMAIL = "alkemy.api.java@gmail.com";
    public static final String LICENCE = "Licencia de API";
    public static final String LICENCE_URL = "www.licence.com";
}
