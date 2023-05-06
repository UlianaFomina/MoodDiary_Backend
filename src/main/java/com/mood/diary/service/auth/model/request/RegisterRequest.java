package com.mood.diary.service.auth.model.request;

import com.mood.diary.service.auth.model.AuthUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email
    private String email;

    @Size(min = 4, max = 10)
    private String username;

    @Size(max = 200)
    private String about;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private AuthUserRole role;

    private String password;
    private String imageUrl;
}
