package com.mood.diary.service.auth.service;

import com.mood.diary.service.auth.model.AuthUser;
import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.repository.AuthUserRepository;
import com.mood.diary.service.auth.service.jwt.JwtService;
import com.mood.diary.service.auth.service.user.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService authUserDetailsService;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        AuthUser user = AuthUser.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .role(registerRequest.getRole())
                .about(registerRequest.getAbout())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .imageUrl(registerRequest.getImageUrl())
                .build();

        AuthUser savedUser = authUserRepository.save(user);

        return token(savedUser);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(token);

        UserDetails user = authUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        return token(user);
    }

    private AuthenticationResponse token(UserDetails userDetails) {
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthenticationResponse(jwtToken);
    }
}
