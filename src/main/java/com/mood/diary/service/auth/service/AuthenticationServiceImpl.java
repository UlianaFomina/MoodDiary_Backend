package com.mood.diary.service.auth.service;

import com.mood.diary.service.auth.constant.EmailTemplate;
import com.mood.diary.service.auth.exception.variants.UserEmailNotConfirmedException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.auth.model.AuthUser;
import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.repository.AuthUserRepository;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.auth.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${server.url}")
    private String serverUrl;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendService emailSendService;
    private final AuthUserRepository authUserRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailConfirmationService emailConfirmationService;

    @Override
    @Transactional
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

        String token = UUID.randomUUID().toString();
        String link = String.format("%s/api/v1/auth/confirm?token=%s", serverUrl, savedUser.getId());
        String emailTemplate = EmailTemplate.buildEmail(savedUser.getUsername(), link);

        emailConfirmationService.putConfirmationToken(savedUser.getId(), token);
        emailSendService.send(savedUser.getEmail(), emailTemplate);

        return token(savedUser);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username, authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(token);

        AuthUser user = authUserRepository
                .findAuthUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username: '%s' not found!", username)));

        boolean isTokenExists = emailConfirmationService.findById(user.getId());

        if(isTokenExists) {
            String exceptionMessage = String.format("User with username: '%s' please confirm your email: '%s'", username, user.getEmail());
            throw new UserEmailNotConfirmedException(exceptionMessage);
        }

        return token(user);
    }

    private AuthenticationResponse token(UserDetails userDetails) {
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthenticationResponse(jwtToken);
    }
}
