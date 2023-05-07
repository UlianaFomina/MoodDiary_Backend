package com.mood.diary.service.auth.controller;

import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.request.ResetPasswordRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.service.AuthenticationService;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.mood.diary.service.auth.service.email.recovery.PasswordRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Authentication Controller",
        description = "Endpoints for authorize / registration / confirm registration token on mail / reset password"
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PasswordRecoveryService passwordRecoveryService;
    private final EmailConfirmationService emailConfirmationService;

    @Operation(
            summary = "User registration",
            description = """
                    Give you ability to create in user in system.
                    After successfully registration you will get verification email.
                    """
    )
    @PostMapping("/registration")
    public AuthenticationResponse register(
            @Parameter(description = "Body of registration request")
            @Valid @RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @Operation(
            summary = "User authorization",
            description = """
                    Give you ability to authorize in system.
                    After successfully authorization you will get JWT token.
                    This token give you ability to get access to secured resources.
                    """
    )
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @Parameter(description = "Body of authenticate request")
            @Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @Operation(
            summary = "Confirm successful registration",
            description = """
                    Token valid for 15 minutes
                    If it's still valid you will be activated.
                    Otherwise you will be needed to repeat verification.
                    """
    )
    @GetMapping("/confirm")
    public String confirm(
            @Parameter(description = "Verification token from email")
            @Valid @NotNull @RequestParam("token") String token) {
        return emailConfirmationService.confirmToken(token);
    }

    @Operation(
            summary = "Start Reset Procedure",
            description = """
                    Start reset procedure means:
                    1. Send email with link
                    2. Click on this link and go to frontend.
                    3. Make frontend steps.
                    """
    )
    @GetMapping("/reset-password")
    public String startResetPasswordProcedure(
            @Parameter(description = "Which email use to reset password procedure")
            @Valid @Email @RequestParam("email") String email) {
        return passwordRecoveryService.startResetPasswordProcedure(email);
    }

    @Operation(
            summary = "Finish Reset Procedure",
            description = """
                    When you try to make last step, literally pass request with password
                    Check here if password not identical to exist one
                    If not and it's valid - finish procedure
                    """
    )
    @PostMapping("/reset-password")
    public String actualResetPassword(
            @Parameter(description = "Body of entity with valid email and password")
            @Valid @RequestBody ResetPasswordRequest request) {
        return passwordRecoveryService.updateResetPassword(request);
    }
}
