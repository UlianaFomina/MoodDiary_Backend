package com.mood.diary.service.auth.service.user;

import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthUserService authUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authUserService.findByUsername(username);
    }
}
