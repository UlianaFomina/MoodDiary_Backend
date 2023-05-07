package com.mood.diary.service.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Reset password request body")
public class ResetPasswordRequest {

    @Email
    @Schema(description = "Email of user. Unique",
            example = "email@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(min = 6, max = 12)
    @Schema(description = "New password of user.",
            example = "newPassword",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 12
    )
    private String newPassword;
}
