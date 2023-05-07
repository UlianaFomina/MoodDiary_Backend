package com.mood.diary.service.auth.service.email.recovery;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.PasswordResetProcedureStartedException;
import com.mood.diary.service.auth.exception.variants.TokenExpiredException;
import com.mood.diary.service.auth.model.request.ResetPasswordRequest;
import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.util.EntityBuilder;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PasswordRecoveryServiceTest extends AbstractServiceTest {

    @Autowired
    PasswordRecoveryService passwordRecoveryService;

    @MockBean
    EmailSendService emailSendService;

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo:4.0.10");

    @Container
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        String redisHost = String.format("redis://%s:%s", redisContainer.getHost(), redisContainer.getMappedPort(6379));
        Supplier<Object> redisSupplier = () -> redisHost;

        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
        registry.add("redis.uri", redisSupplier);
    }

    @Test
    void startResetPasswordProcedure_valid() {
        AuthUser savedUser = initDefaultUser("username", "email@gmail.com");

        doNothing().when(emailSendService).send(any(), any());
        String response = passwordRecoveryService.startResetPasswordProcedure(savedUser.getEmail());

        assertThat(response).isEqualTo("Please check your email for reset password link!");
    }

    @Test
    void startResetPasswordProcedure_throwProcedureAlreadyStarted() {
        AuthUser savedUser = initDefaultUser("username", "email@gmail.com");

        doNothing().when(emailSendService).send(any(), any());
        String response = passwordRecoveryService.startResetPasswordProcedure(savedUser.getEmail());

        assertThat(response).isEqualTo("Please check your email for reset password link!");
        assertThatExceptionOfType(PasswordResetProcedureStartedException.class)
                .isThrownBy(() -> passwordRecoveryService.startResetPasswordProcedure(savedUser.getEmail()));
    }

    @Test
    void updateResetPassword_pass() {
        String email = "email@gmail.com";
        initDefaultUser("username", email);
        ResetPasswordRequest request = EntityBuilder.makeResetRequest(email, "newPassword");

        assertThatExceptionOfType(TokenExpiredException.class)
                .isThrownBy(() -> passwordRecoveryService.updateResetPassword(request));
    }

    @Test
    void getByEmail() {
    }

    @Test
    void resetEmail() {
    }
}