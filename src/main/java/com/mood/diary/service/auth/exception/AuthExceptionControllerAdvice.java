package com.mood.diary.service.auth.exception;

import com.mood.diary.service.auth.exception.variants.UserEmailNotConfirmedException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class AuthExceptionControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFoundException(UserNotFoundException userNotFoundException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                userNotFoundException.getMessage(),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler(UserEmailNotConfirmedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage userEmailNotConfirmedException(UserEmailNotConfirmedException userNotFoundException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                userNotFoundException.getMessage(),
                webRequest.getDescription(false)
        );
    }
}
