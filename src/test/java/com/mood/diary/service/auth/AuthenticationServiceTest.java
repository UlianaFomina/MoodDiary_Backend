package com.mood.diary.service.auth;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.PasswordMustNotBeEqualsPreviousException;
import com.mood.diary.service.auth.exception.variants.UserAlreadyExistsException;
import com.mood.diary.service.auth.exception.variants.UserEmailNotConfirmedException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.service.AuthenticationService;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import com.mood.diary.service.util.EntityBuilder;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AuthenticationServiceTest extends AbstractServiceTest {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EmailConfirmationService emailConfirmationService;

    @Autowired
    AuthUserService authUserService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    EmailSendService emailSendService;

    @MockBean
    AuthenticationManager authenticationManager;

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
    void register_pass() {
        RegisterRequest registerRequest = EntityBuilder.makeRegister(
                "username", "email@gmail.com",
                "pass", LocalDate.now(), "about", "url"
        );

        doNothing().when(emailSendService).send(any(), any(), any());

        AuthenticationResponse response = authenticationService.register(registerRequest);
        RMapCache<Object, Object> verificationTokens = redissonClient.getMapCache("confirmation-tokens");

        assertThat(response.token()).isNotNull();
        assertThat(verificationTokens).hasSize(1);
    }

    @Test
    void register_throwUserAlreadyExistsException() {
        String username = "username";
        String email = "email@gmail.com";
        AuthUser authUser = AuthUser.builder()
                .username(username)
                .email(email)
                .build();
        authUserRepository.save(authUser);

        RegisterRequest registerRequest = EntityBuilder.makeRegister(
                username, email,
                "pass", LocalDate.now(), "about", "url"
        );

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> authenticationService.register(registerRequest));
    }

    @Test
    void authenticate_throwUserEmailNotConfirmedException() {
        String username = "username";
        String email = "email@gmail.com";
        String password = "password";
        RegisterRequest registerRequest = EntityBuilder.makeRegister(
                username, email,
                password, LocalDate.now(), "about", "url"
        );
        doNothing().when(emailSendService).send(any(), any(), any());
        authenticationService.register(registerRequest);

        AuthenticationRequest request = EntityBuilder.makeAuthRequest(username, password);

        assertThatExceptionOfType(UserEmailNotConfirmedException.class)
                .isThrownBy(() -> authenticationService.authenticate(request));
    }

    @Test
    void authenticate_pass() {
        String username = "username";
        String email = "email@gmail.com";
        String password = "password";
        RegisterRequest registerRequest = EntityBuilder.makeRegister(
                username, email,
                password, LocalDate.now(), "about", "url"
        );
        doNothing().when(emailSendService).send(any(), any(), any());
        authenticationService.register(registerRequest);

        AuthUser user = authUserService.findByUsername(username);
        emailConfirmationService.confirmToken(user.getId());

        AuthenticationRequest request = EntityBuilder.makeAuthRequest(username, password);
        AuthenticationResponse response = authenticationService.authenticate(request);

        assertThat(response.token()).isNotNull();
    }

    @Test
    void resetPassword_pass() {
        String email = "email@gmail.com";
        AuthUser newUser = initDefaultUser("username", email);
        String newPassword = "newPassword";

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        authenticationService.resetPassword(newUser, newPassword);
        AuthUser dbUser = authUserService.findByEmail(email);

        assertThat(dbUser.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void authenticate_throwUserNotFoundException() {
        AuthenticationRequest request = EntityBuilder.makeAuthRequest("username", "password");

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> authenticationService.authenticate(request));
    }

    @Test
    void resetPassword_throwNewPasswordIdenticalToNew() {
        String email = "email@gmail.com";
        AuthUser newUser = initDefaultUser("username", email);

        assertThatExceptionOfType(PasswordMustNotBeEqualsPreviousException.class)
                .isThrownBy(() -> authenticationService.resetPassword(newUser, newUser.getPassword()));
    }

    @Test
    void resetPassword_userNotFound() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> authenticationService.resetPassword(new AuthUser(), ""));
    }
}
