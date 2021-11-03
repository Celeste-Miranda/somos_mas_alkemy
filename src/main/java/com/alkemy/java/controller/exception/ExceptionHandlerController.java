package com.alkemy.java.controller.exception;

import com.alkemy.java.dto.ErrorDataMessageDto;
import com.alkemy.java.dto.ErrorMessageDto;
import com.alkemy.java.exception.EmailNotSentException;
import com.alkemy.java.exception.ConflictException;
import com.alkemy.java.exception.ForbiddenException;
import javassist.NotFoundException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import static com.alkemy.java.util.Constants.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Value("error.request.body.missing")
    private String messageRequestBodyMissing;

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageDto usernameNotFoundException(UsernameNotFoundException ex) {
        return new ErrorMessageDto(new Date(), USERNAME_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleNotFoundException(NotFoundException exception){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageDto.builder()
                .timestamp(new Date())
                .exception(NOT_FOUND)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageDto resourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorMessageDto(new Date(),"ResourceNotFoundException", ex.getMessage());
    }

    @ExceptionHandler(value = EmailNotSentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto emailNotSendException(EmailNotSentException ex) {
        return new ErrorMessageDto(new Date(), EMAIL_NOT_SENT, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
            String field =((FieldError) error).getField();
            String message=error.getDefaultMessage();
            errors.put(field,message);
        });

        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto BadRequestException(BadRequestException ex) {
        return new ErrorMessageDto(new Date(),BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = InvalidDataException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
      public ErrorDataMessageDto InvalidDataException(InvalidDataException exc) {

    List<String> errors = exc.getResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());
    return new ErrorDataMessageDto (new Date(),INVALID_DATA,errors);
    }

    @ExceptionHandler (value = ForbiddenException.class)
    @ResponseStatus (value = HttpStatus.FORBIDDEN)
    public ErrorMessageDto forbiddenException(ForbiddenException ex) {
    return new ErrorMessageDto (new Date(),FORBIDDEN,ex.getMessage());
    }

    @ExceptionHandler(value = ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessageDto conflictException(ConflictException ex){
        return new ErrorMessageDto(new Date(),CONFLICT,ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessageDto error = new ErrorMessageDto(new Date(), NOT_READABLE,
                messageSource.getMessage(messageRequestBodyMissing, null, Locale.getDefault()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto argumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName().substring(10);
        return new ErrorMessageDto (new Date(),ARGUMENT_TYPE_MISMATCH, error);
    }

    @ExceptionHandler (value = ConstraintViolationException.class)
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public ErrorDataMessageDto constraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        return new ErrorDataMessageDto (new Date(),CONSTRAINT_VIOLATION, errors);
    }

    @ExceptionHandler (value =  RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageDto runtimeException (RuntimeException ex){
        return new ErrorMessageDto(new Date(),BAD_REQUEST,ex.getMessage());
    }

}
