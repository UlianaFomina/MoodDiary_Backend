package com.mood.diary.service.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResetPasswordRequest {
    @Email
    private String email;

    @Size(min = 6, max = 12)
    private String newPassword;
}
