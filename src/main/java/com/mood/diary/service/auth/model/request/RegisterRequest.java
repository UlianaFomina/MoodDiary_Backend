package com.mood.diary.service.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Registration request body")
public class RegisterRequest {

    @Email
    @Schema(description = "Email of user. Unique",
            example = "email@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(min = 4, max = 10)
    @Schema(description = "Username of user. Unique",
            example = "username",
            minLength = 4,
            maxLength = 10,
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Size(max = 200)
    @Schema(description = "Short information about user",
            example = """
                    Passionate learner, creative thinker, and avid explorer.
                    Embracing life's challenges, seeking growth, and spreading positivity
                    Dreamer, doer, and lover of all things art.
                    """, maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String about;

    @NotNull
    @Schema(description = "Date of birth of user",
            example = "2003-02-17",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dateOfBirth;

    @Size(min = 6, max = 12)
    @Schema(description = "Password of user",
            example = "qwerty",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 12
    )
    private String password;

    @Schema(description = "ImageUrl to user avatar",
            example = "'avatarUrl'",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String imageUrl;
}
