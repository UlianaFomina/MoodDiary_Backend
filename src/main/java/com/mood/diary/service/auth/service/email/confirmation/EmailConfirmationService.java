package com.mood.diary.service.auth.service.email.confirmation;

public interface EmailConfirmationService {
    boolean findById(String id);
    void putConfirmationToken(String id, String token);
    String confirmToken(String token);
}
