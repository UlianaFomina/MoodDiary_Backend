package com.mood.diary.service.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticationRequest {

    @Size(min = 4, max = 10)
    @Schema(description = "Username of user to login in system",
            example = "username",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Size(min = 6, max = 12)
    @Schema(description = "Password of user.",
            example = "password",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 12
    )
    private String password;
}
