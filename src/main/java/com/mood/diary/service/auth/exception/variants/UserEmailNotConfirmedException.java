package com.mood.diary.service.auth.exception.variants;

public class UserEmailNotConfirmedException extends RuntimeException {

    public UserEmailNotConfirmedException(String message) {
        super(message);
    }
}
