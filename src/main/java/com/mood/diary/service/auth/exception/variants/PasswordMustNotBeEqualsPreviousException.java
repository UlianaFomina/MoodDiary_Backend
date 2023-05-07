package com.mood.diary.service.auth.exception.variants;

public class PasswordMustNotBeEqualsPreviousException extends RuntimeException {

    public PasswordMustNotBeEqualsPreviousException(String message) {
        super(message);
    }
}
