package com.mood.diary.service.auth.service.email.recovery;

import com.mood.diary.service.auth.exception.variants.PasswordResetProcedureStartedException;
import com.mood.diary.service.auth.exception.variants.TokenExpiredException;
import com.mood.diary.service.auth.model.request.ResetPasswordRequest;
import com.mood.diary.service.auth.service.AuthenticationService;
import com.mood.diary.service.auth.service.email.parse.EmailParseTemplateService;
import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private static final String RESET_PASSWORD_TOKENS = "reset-password-tokens";

    @Value("${frontend.url}")
    private String frontendUrl;

    private final RedissonClient redissonClient;
    private final AuthUserService authUserService;
    private final EmailSendService emailSendService;
    private final AuthenticationService authenticationService;
    private final EmailParseTemplateService emailParseTemplateService;

    @Override
    @Transactional
    public String startResetPasswordProcedure(String email) {
        String token = UUID.randomUUID().toString();
        authUserService.findByEmail(email);

        RMapCache<Object, Object> resetPasswordMap = redissonClient.getMapCache(RESET_PASSWORD_TOKENS);
        Object dbToken = resetPasswordMap.get(email);
        if(dbToken != null) {
            throw new PasswordResetProcedureStartedException("You already started password recovery procedure!");
        }

        resetPasswordMap.put(email, token, 15L, TimeUnit.MINUTES);

        String link = String.format("%s/resetPassword?token=%s", frontendUrl, token);
        String emailTemplate = emailParseTemplateService.getResetPasswordTemplate(email, link);

        emailSendService.send(email, emailTemplate);

        return "Please check your email for reset password link!";
    }

    @Override
    public String updateResetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        AuthUser dbUser = authUserService.findByEmail(email);

        String dbToken = getByEmail(email);
        if(dbToken == null) {
            throw new TokenExpiredException("Password recovery link expired!");
        }

        resetEmail(email);
        authenticationService.resetPassword(dbUser, resetPasswordRequest.getNewPassword());

        return "Password successfully changed!";
    }

    @Override
    public String getByEmail(String email) {
        Object token = redissonClient
                .getMapCache(RESET_PASSWORD_TOKENS)
                .get(email);
        return token != null
                ? token.toString()
                : null;
    }

    @Override
    public void resetEmail(String email) {
        RMapCache<Object, Object> dbMap = redissonClient.getMapCache(RESET_PASSWORD_TOKENS);
        Object dbToken = dbMap.get(email);

        if(dbToken != null) {
            dbMap.remove(email);
            return;
        }

        throw new TokenExpiredException("Your token to reset password is expired!");
    }
}
