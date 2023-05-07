package com.mood.diary.service.auth.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication / Registration response - JWT token")
public record AuthenticationResponse(
        @Schema(description = "JSON Web Token", example = "jwt-token")
        String token
) {}
