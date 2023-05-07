package com.mood.diary.service.util;

import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.request.ResetPasswordRequest;
import com.mood.diary.service.story.model.Story;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.model.AuthUserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class EntityBuilder {

    private EntityBuilder() {}

    public static RegisterRequest makeRegister(String username,
                                       String email,
                                       String password,
                                       LocalDate dateOfBirth,
                                       String about,
                                       String imageUrl) {
        return RegisterRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .dateOfBirth(dateOfBirth)
                .about(about)
                .imageUrl(imageUrl)
                .build();
    }

    public static AuthenticationRequest makeAuthRequest(String username, String password) {
        return AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static AuthUser makeAuthUser(String username,
                                        String password,
                                        String email,
                                        String about,
                                        String imageUrl) {
        return AuthUser.builder()
                .id(null)
                .username(username)
                .email(email)
                .password(password)
                .role(AuthUserRole.USER)
                .dateOfBirth(LocalDate.now())
                .about(about)
                .imageUrl(imageUrl)
                .stories(new ArrayList<>())
                .build();
    }

    public static ResetPasswordRequest makeResetRequest(String email, String newPassword) {
        return ResetPasswordRequest.builder()
                .email(email)
                .newPassword(newPassword)
                .build();
    }

    public static Story buildWithTime(String userId, String content, LocalDateTime createdAt) {
        return Story.builder()
                .userId(userId)
                .id(UUID.randomUUID().toString())
                .content(content)
                .updatedAt(LocalDateTime.now())
                .createdAt(createdAt)
                .satisfactionRate(0.4)
                .build();
    }
}
