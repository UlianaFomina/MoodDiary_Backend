package com.mood.diary.service.auth.service.user;

import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authUserRepository
                .findAuthUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username: '%s' not found!", username)));
    }
}
