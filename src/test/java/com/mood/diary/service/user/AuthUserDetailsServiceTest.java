package com.mood.diary.service.user;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.auth.service.user.AuthUserDetailsService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AuthUserDetailsServiceTest extends AbstractServiceTest {

    @Autowired
    AuthUserDetailsService authUserDetailsService;

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
    void loadUserByUsername_pass() {
        String username = "username";
        String email = "email@gmail.com";
        initDefaultUser(username, email);

        UserDetails userDetails = authUserDetailsService.loadUserByUsername(username);

        assertThat(userDetails.getUsername()).isEqualTo(username);
    }

    @Test
    void loadUserByUsername_throwUserNotFoundException() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> authUserDetailsService.loadUserByUsername("notExistUsername"));
    }
}