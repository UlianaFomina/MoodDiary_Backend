package com.mood.diary.service.auth.model.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticationRequest {
    @Size(min = 4, max = 10)
    private String username;

    @Size(min = 6, max = 12)
    private String password;
}
