package com.example.basic_crm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegisterErrorAdvice {

    @ExceptionHandler(RegisterErrorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse registerErrorHandler(RegisterErrorException ex) {
        return new ExceptionResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
}
