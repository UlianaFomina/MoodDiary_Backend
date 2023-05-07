package com.mood.diary.service.exception;

import com.mood.diary.service.auth.exception.ErrorDetail;
import com.mood.diary.service.auth.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, WebRequest webRequest) {
        List<ErrorDetail> errors = methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(e -> {
                    String errorMessage = String.format("%s: %s", e.getField(), e.getDefaultMessage());
                    return new ErrorDetail(errorMessage);
                })
                .toList();

        return new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                new Date(),
                errors,
                webRequest.getDescription(false)
        );
    }
}
