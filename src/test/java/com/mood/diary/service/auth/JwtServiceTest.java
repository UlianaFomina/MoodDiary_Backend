package com.mood.diary.service.auth;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.model.request.AuthenticationRequest;
import com.mood.diary.service.auth.model.request.RegisterRequest;
import com.mood.diary.service.auth.model.response.AuthenticationResponse;
import com.mood.diary.service.auth.service.AuthenticationService;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.mood.diary.service.auth.service.email.send.EmailSendService;
import com.mood.diary.service.auth.service.jwt.JwtService;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import com.mood.diary.service.util.EntityBuilder;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtServiceTest extends AbstractServiceTest {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AuthUserService authUserService;

    @Autowired
    EmailConfirmationService emailConfirmationService;

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
    void extractUsername() {
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

        String extractedUsername = jwtService.extractUsername(response.token());

        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void generateToken() {
        AuthUser user = AuthUser.builder()
                .username("username")
                .id("id")
                .build();

        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull();
    }

    @Test
    void isTokenValid() {
        AuthUser user = AuthUser.builder()
                .username("username")
                .id("id")
                .build();

        String token = jwtService.generateToken(user);
        boolean tokenValid = jwtService.isTokenValid(token, user);

        assertThat(tokenValid).isTrue();
    }
}
