package com.mood.diary.service.auth.service.email.parse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailParseTemplateServiceImpl implements EmailParseTemplateService {

    private final TemplateEngine templateEngine;

    @Override
    public String getVerificationTemplate(String username, String link) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("link", link);

        return templateEngine.process("verification-email", context);
    }

    @Override
    public String getResetPasswordTemplate(String email, String link) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("link", link);

        return templateEngine.process("password-reset", context);
    }
}
