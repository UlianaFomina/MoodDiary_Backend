package com.mood.diary.service.user.service;

import com.mood.diary.service.user.model.AuthUser;
import org.springframework.web.multipart.MultipartFile;

public interface AuthUserService {
    AuthUser findByEmail(String email);
    AuthUser findByUsername(String username);
    AuthUser findById(String id);
    AuthUser save(AuthUser authUser);
    void validateUniqueUsernameAndEmail(String username, String email);
    void attachAvatar(String id, MultipartFile avatar);
}
