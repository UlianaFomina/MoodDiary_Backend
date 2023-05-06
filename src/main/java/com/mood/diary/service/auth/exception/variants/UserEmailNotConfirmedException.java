package com.mood.diary.service.auth.exception.variants;

public class UserEmailNotConfirmedException extends RuntimeException {

    public UserEmailNotConfirmedException() {}

    public UserEmailNotConfirmedException(String message) {
        super(message);
    }

    public UserEmailNotConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailNotConfirmedException(Throwable cause) {
        super(cause);
    }
}
