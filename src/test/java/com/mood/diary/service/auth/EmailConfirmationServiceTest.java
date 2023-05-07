package com.mood.diary.service.auth;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.TokenExpiredException;
import com.mood.diary.service.auth.service.email.confirmation.EmailConfirmationService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailConfirmationServiceTest extends AbstractServiceTest {

    @Autowired
    EmailConfirmationService emailConfirmationService;

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
    void findById_true() {
        String idKey = "id";
        emailConfirmationService.putConfirmationToken(idKey, "value");

        boolean isExists = emailConfirmationService.findById(idKey);

        assertThat(isExists).isTrue();
    }

    @Test
    void findById_false() {
        String idKey = "id";
        emailConfirmationService.putConfirmationToken(idKey, "value");

        boolean isExists = emailConfirmationService.findById("notExistsId");

        assertThat(isExists).isFalse();
    }

    @Test
    void putConfirmationToken() {
        String idKey = "randomId";
        emailConfirmationService.putConfirmationToken(idKey, "randomValue");

        RMapCache<Object, Object> map = redissonClient.getMapCache("confirmation-tokens");
        Object withTTLOnly = map.getWithTTLOnly(idKey);

        assertThat(withTTLOnly).isNotNull();
    }

    @Test
    void confirmToken_activated() {
        String idKey = "id";
        emailConfirmationService.putConfirmationToken(idKey, "value");

        String response = emailConfirmationService.confirmToken(idKey);

        assertThat(response).isEqualTo("You account successfully activated!");
    }

    @Test
    void confirmToken_expired() {
        String idKey = "id";
        emailConfirmationService.putConfirmationToken(idKey, "value");

        String response = emailConfirmationService.confirmToken(idKey);

        assertThat(response).isEqualTo("You account successfully activated!");
        assertThatExceptionOfType(TokenExpiredException.class)
                .isThrownBy(() -> emailConfirmationService.confirmToken(idKey));
    }
}
