package com.mood.diary.service.user.service;

import com.mood.diary.service.auth.exception.variants.UserAlreadyExistsException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;

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
}
