package com.mood.diary.service.auth;

import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.auth.service.email.send.EmailSendServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailSendServiceTest {

    @Mock JavaMailSender javaMailSender;

    EmailSendService emailSendService;

    @BeforeEach
    void init() {
        emailSendService = new EmailSendServiceImpl(javaMailSender);
    }

    @Test
    void send_success() {
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSendService.send("email@gmail.com", "", "");

        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void send_addressException() {
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> emailSendService.send("", "", ""));
    }
}