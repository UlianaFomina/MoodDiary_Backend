package com.mood.diary.service.auth.service;

import com.mood.diary.service.auth.exception.variants.PasswordMustNotBeEqualsPreviousException;
import com.mood.diary.service.auth.exception.variants.UserEmailNotConfirmedException;
import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.mood.diary.service.auth.service.email.parse.EmailParseTemplateServiceImpl;
import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.auth.service.jwt.JwtService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.model.AuthUserRole;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${frontend.url}")
    private String frontendUrl;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserService authUserService;
    private final EmailSendService emailSendService;
    private final EmailParseTemplateServiceImpl emailParseService;
    private final AuthenticationManager authenticationManager;
    private final EmailConfirmationService emailConfirmationService;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        String email = registerRequest.getEmail();
        String username = registerRequest.getUsername();
        AuthUser user = AuthUser.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(AuthUserRole.USER)
                .about(registerRequest.getAbout())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .imageUrl(registerRequest.getImageUrl())
                .build();

        authUserService.validateUniqueUsernameAndEmail(username, email);

        AuthUser savedUser = authUserService.save(user);

        String token = UUID.randomUUID().toString();
        String link = String.format("%s/verification?token=%s", frontendUrl, savedUser.getId());
        String emailTemplate = emailParseService.getVerificationTemplate(savedUser.getUsername(), link);

        emailConfirmationService.putConfirmationToken(savedUser.getId(), token);
        emailSendService.send(savedUser.getEmail(), emailTemplate, "Verify your email!");

        return token(savedUser);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username, authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(token);

        AuthUser user = authUserService.findByUsername(username);

        boolean isTokenExists = emailConfirmationService.findById(user.getId());

        if(isTokenExists) {
            String exceptionMessage = String.format("User with username: '%s' please confirm your email: '%s'", username, user.getEmail());
            throw new UserEmailNotConfirmedException(exceptionMessage);
        }

        return token(user);
    }

    @Override
    public void resetPassword(AuthUser authUser, String newPassword) {
        AuthUser dbUser = authUserService.findByEmail(authUser.getEmail());
        String encodedPassword = passwordEncoder.encode(newPassword);

        if(dbUser.getPassword().equals(newPassword)) {
            throw new PasswordMustNotBeEqualsPreviousException("New password must not be identical to the new one!");
        }
        dbUser.setPassword(encodedPassword);

        authUserService.save(dbUser);
    }

    private AuthenticationResponse token(AuthUser authUser) {
        String jwtToken = jwtService.generateToken(authUser);

        return new AuthenticationResponse(jwtToken);
    }
}
