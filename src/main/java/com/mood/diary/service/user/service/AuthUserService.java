package com.mood.diary.service.user.service;

import com.mood.diary.service.user.model.AuthUser;

public interface AuthUserService {
    AuthUser findByEmail(String email);
    AuthUser findByUsername(String username);
    AuthUser save(AuthUser authUser);
    void validateUniqueUsernameAndEmail(String username, String email);
}
