package com.mood.diary.service.auth.model.request;

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
public class RegisterRequest {

    @Email
    private String email;

    @Size(min = 4, max = 10)
    private String username;

    @Size(max = 200)
    private String about;

    @NotNull
    private LocalDate dateOfBirth;

    @Size(min = 6, max = 12)
    private String password;

    private String imageUrl;
}
