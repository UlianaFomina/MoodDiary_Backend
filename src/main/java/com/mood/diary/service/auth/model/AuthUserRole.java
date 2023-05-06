package com.mood.diary.service.auth.model;

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
