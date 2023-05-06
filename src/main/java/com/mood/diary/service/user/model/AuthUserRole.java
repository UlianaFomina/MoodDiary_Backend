package com.mood.diary.service.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AuthUserRole {
    USER, PSYCHOLOGIST;

    @JsonCreator
    public static AuthUserRole fromValue(String value) {
        for (AuthUserRole role : AuthUserRole.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}
