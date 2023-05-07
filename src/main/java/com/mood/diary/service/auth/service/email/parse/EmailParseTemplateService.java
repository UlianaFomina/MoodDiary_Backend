package com.mood.diary.service.auth.service.email.parse;

public interface EmailParseTemplateService {
    String getVerificationTemplate(String username, String link);
    String getResetPasswordTemplate(String email, String link);
}
