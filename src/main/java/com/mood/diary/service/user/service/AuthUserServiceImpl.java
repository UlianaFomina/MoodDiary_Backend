package com.mood.diary.service.user.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mood.diary.service.auth.exception.variants.UserAlreadyExistsException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.repository.AuthUserRepository;
import io.netty.util.internal.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final Cloudinary cloudinary;

    @Override
    public AuthUser findByEmail(String email) {
        return authUserRepository
                .findAuthUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email: '%s' not found!", email)));
    }

    @Override
    public AuthUser findByUsername(String username) {
        return authUserRepository
                .findAuthUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username: '%s' not found!", username)));
    }

    @Override
    public AuthUser findById(String id) {
        return authUserRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: '%s' not found!", id)));
    }

    @Override
    public AuthUser save(AuthUser authUser) {
        return authUserRepository.save(authUser);
    }

    @Override
    public void validateUniqueUsernameAndEmail(String username, String email) {
        authUserRepository.findAuthUserByEmail(email)
                .ifPresent(e -> {
                    String exceptionMessage = String.format("User with this email: '%s' already exists, please login!", email);
                    throw new UserAlreadyExistsException(exceptionMessage);
                });

        authUserRepository.findAuthUserByUsername(username)
                .ifPresent(e -> {
                    String exceptionMessage = String.format("User with this username: '%s' already exists, please login!", username);
                    throw new UserAlreadyExistsException(exceptionMessage);
                });
    }

    @Override
    public void attachAvatar(String id, MultipartFile avatar) {
        AuthUser user = findById(id);
        try {
            Map uploadResults = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
            String imageId = uploadResults.get("secure_url").toString();
            user.setImageUrl(imageId);

            save(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
