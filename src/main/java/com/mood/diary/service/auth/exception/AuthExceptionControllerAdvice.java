package com.mood.diary.service.auth.exception;

import com.mood.diary.service.auth.exception.variants.*;
import com.mood.diary.service.exception.model.ErrorDetail;
import com.mood.diary.service.exception.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class AuthExceptionControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFoundException(UserNotFoundException userNotFoundException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                List.of(new ErrorDetail(userNotFoundException.getMessage())),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler(UserEmailNotConfirmedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage userEmailNotConfirmedException(UserEmailNotConfirmedException userEmailNotConfirmedException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                List.of(new ErrorDetail(userEmailNotConfirmedException.getMessage())),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage userAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                List.of(new ErrorDetail(userAlreadyExistsException.getMessage())),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage tokenExpiredException(TokenExpiredException tokenExpiredException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                List.of(new ErrorDetail(tokenExpiredException.getMessage())),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler(PasswordResetProcedureStartedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage passwordResetProcedureStartedException(PasswordResetProcedureStartedException passwordResetProcedureStartedException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                List.of(new ErrorDetail(passwordResetProcedureStartedException.getMessage())),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler(PasswordMustNotBeEqualsPreviousException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage passwordMustNotBeEqualsPreviousException(PasswordMustNotBeEqualsPreviousException PasswordMustNotBeEqualsPreviousException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                List.of(new ErrorDetail(PasswordMustNotBeEqualsPreviousException.getMessage())),
                webRequest.getDescription(false)
        );
    }
}
