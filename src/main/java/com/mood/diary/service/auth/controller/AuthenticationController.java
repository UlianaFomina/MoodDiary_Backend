package com.mood.diary.service.auth.controller;

import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.request.ResetPasswordRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.service.AuthenticationService;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.mood.diary.service.auth.service.email.recovery.PasswordRecoveryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PasswordRecoveryService passwordRecoveryService;
    private final EmailConfirmationService emailConfirmationService;

    @PostMapping("/registration")
    public AuthenticationResponse register(@Valid @RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping("/confirm")
    public String confirm(@Valid @NotNull @RequestParam("token") String token) {
        return emailConfirmationService.confirmToken(token);
    }

    @GetMapping("/reset-password")
    public String startResetPasswordProcedure(@Valid @Email @RequestParam("email") String email) {
        return passwordRecoveryService.startResetPasswordProcedure(email);
    }

    @PostMapping("/reset-password")
    public String actualResetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return passwordRecoveryService.updateResetPassword(request);
    }
}
