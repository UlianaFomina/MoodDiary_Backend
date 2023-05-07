package com.mood.diary.service.auth.service.email.recovery;

import com.mood.diary.service.auth.model.request.ResetPasswordRequest;

public interface PasswordRecoveryService {
    String startResetPasswordProcedure(String email);
    String updateResetPassword(ResetPasswordRequest resetPasswordRequest);
    String getByEmail(String token);
    void resetEmail(String token);
}
