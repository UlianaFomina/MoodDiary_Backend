package com.mood.diary.service.auth.service.email.send;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendServiceImpl implements EmailSendService {

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("MoodDiary: Confirm your email");

            helper.setFrom("zhenek02ss@gmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Fail to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
